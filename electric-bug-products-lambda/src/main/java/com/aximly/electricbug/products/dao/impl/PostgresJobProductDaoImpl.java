package com.aximly.electricbug.products.dao.impl;

import com.aximly.electricbug.products.dao.JobProductDao;
import com.aximly.electricbug.products.dto.JobProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobProductDaoImpl implements JobProductDao {

    private final DataSource cloudDataSource;

    public PostgresJobProductDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobProductDto mapRow(ResultSet rs) throws SQLException {
        JobProductDto dto = new JobProductDto();
        dto.setId(rs.getInt("id"));
        dto.setJobId(rs.getInt("job_id"));
        Object stockId = rs.getObject("stock_id");
        dto.setStockId(stockId != null ? rs.getDouble("stock_id") : null);
        dto.setProductName(rs.getString("product_name"));
        dto.setBarcode(rs.getString("barcode"));
        Object qty = rs.getObject("qty");
        dto.setQty(qty != null ? rs.getDouble("qty") : null);
        dto.setPrice(rs.getDouble("price"));
        dto.setLineTotal(rs.getDouble("line_total"));
        return dto;
    }

    @Override
    public List<JobProductDto> getByJobId(Integer jobId) {
        List<JobProductDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_products WHERE job_id = ? ORDER BY id")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read products from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobProductDto> getById(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_products WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read product from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobProductDto create(JobProductDto dto) {
        String sql = "INSERT INTO job_products (job_id, stock_id, product_name, barcode, qty, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getJobId());
            if (dto.getStockId() != null) ps.setDouble(2, dto.getStockId()); else ps.setNull(2, Types.DOUBLE);
            ps.setString(3, dto.getProductName());
            ps.setString(4, dto.getBarcode());
            if (dto.getQty() != null) ps.setDouble(5, dto.getQty()); else ps.setNull(5, Types.DOUBLE);
            ps.setDouble(6, dto.getPrice() != null ? dto.getPrice() : 0.0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dto.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert product into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobProductDto dto) {
        String sql = "UPDATE job_products SET stock_id = ?, product_name = ?, barcode = ?, qty = ?, price = ? WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (dto.getStockId() != null) ps.setDouble(1, dto.getStockId()); else ps.setNull(1, Types.DOUBLE);
            ps.setString(2, dto.getProductName());
            ps.setString(3, dto.getBarcode());
            if (dto.getQty() != null) ps.setDouble(4, dto.getQty()); else ps.setNull(4, Types.DOUBLE);
            ps.setDouble(5, dto.getPrice() != null ? dto.getPrice() : 0.0);
            ps.setInt(6, dto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_products WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product from Postgres: " + e.getMessage(), e);
        }
    }
}