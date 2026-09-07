package com.aximly.electricbug.stock.dao.impl;

import com.aximly.electricbug.stock.dao.StockDao;
import com.aximly.electricbug.stock.dto.StockDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "externalApiFlag", havingValue = "false", matchIfMissing = true)
public class PostgresStockDaoImpl implements StockDao {

    private final DataSource cloudDataSource;

    public PostgresStockDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private StockDto mapRow(ResultSet rs) throws SQLException {
        StockDto dto = new StockDto();
        dto.setStockId(rs.getInt("stock_id"));
        dto.setBarcode(rs.getString("barcode"));
        dto.setDescription(rs.getString("description"));
        dto.setSellPrice(rs.getBigDecimal("sell_price"));
        dto.setCostPrice(rs.getBigDecimal("cost_price"));
        dto.setQuantity(rs.getInt("quantity"));
        Object deptId = rs.getObject("dept_id");
        dto.setDeptId(deptId != null ? rs.getInt("dept_id") : null);
        return dto;
    }

    @Override
    public List<StockDto> getAllStock() {
        List<StockDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM stock ORDER BY description")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read stock from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<StockDto> getStockById(Integer stockId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM stock WHERE stock_id = ?")) {
            ps.setInt(1, stockId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read stock from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<StockDto> getStockByBarcode(String barcode) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM stock WHERE barcode = ?")) {
            ps.setString(1, barcode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read stock from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<StockDto> searchStock(String query) {
        List<StockDto> list = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE description ILIKE ? OR barcode ILIKE ? ORDER BY description LIMIT 50";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search stock in Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<StockDto> getStockByDept(Integer deptId) {
        List<StockDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM stock WHERE dept_id = ? ORDER BY description")) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read stock from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public StockDto createStock(StockDto stock) {
        String sql = "INSERT INTO stock (barcode, description, sell_price, cost_price, quantity, dept_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, stock.getBarcode());
            ps.setString(2, stock.getDescription());
            ps.setBigDecimal(3, stock.getSellPrice());
            ps.setBigDecimal(4, stock.getCostPrice());
            ps.setInt(5, stock.getQuantity() != null ? stock.getQuantity() : 0);
            if (stock.getDeptId() != null) ps.setInt(6, stock.getDeptId());
            else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) stock.setStockId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert stock into Postgres: " + e.getMessage(), e);
        }
        return stock;
    }

    @Override
    public boolean updateStock(StockDto stock) {
        String sql = "UPDATE stock SET barcode = ?, description = ?, sell_price = ?, cost_price = ?, " +
                "quantity = ?, dept_id = ? WHERE stock_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stock.getBarcode());
            ps.setString(2, stock.getDescription());
            ps.setBigDecimal(3, stock.getSellPrice());
            ps.setBigDecimal(4, stock.getCostPrice());
            ps.setInt(5, stock.getQuantity() != null ? stock.getQuantity() : 0);
            if (stock.getDeptId() != null) ps.setInt(6, stock.getDeptId());
            else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, stock.getStockId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update stock in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteStock(Integer stockId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM stock WHERE stock_id = ?")) {
            ps.setInt(1, stockId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete stock from Postgres: " + e.getMessage(), e);
        }
    }
}