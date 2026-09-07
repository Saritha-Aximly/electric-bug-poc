package com.aximly.electricbug.order.dao.impl;

import com.aximly.electricbug.order.dao.OrderDao;
import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;
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
public class PostgresOrderDaoImpl implements OrderDao {

    private final DataSource cloudDataSource;

    public PostgresOrderDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private OrderDto mapOrderRow(ResultSet rs) throws SQLException {
        OrderDto dto = new OrderDto();
        dto.setOrderId(rs.getInt("order_id"));
        dto.setRevision((int) rs.getShort("revision"));
        dto.setOrderDate(rs.getTimestamp("order_date") != null
                ? rs.getTimestamp("order_date").toLocalDateTime() : null);
        dto.setDueDate(rs.getTimestamp("due_date") != null
                ? rs.getTimestamp("due_date").toLocalDateTime() : null);
        dto.setStaffId(rs.getInt("staff_id"));
        dto.setSupplierId(rs.getInt("supplier_id"));
        dto.setOrderSuffix(rs.getString("order_suffix"));
        dto.setComments(rs.getString("comments"));
        dto.setArchive(rs.getBoolean("archive"));
        return dto;
    }

    private LaybyOrderDto mapLaybyRow(ResultSet rs) throws SQLException {
        LaybyOrderDto dto = new LaybyOrderDto();
        dto.setLaybyId(rs.getInt("layby_id"));
        dto.setLaybyDate(rs.getTimestamp("layby_date") != null
                ? rs.getTimestamp("layby_date").toLocalDateTime() : null);
        dto.setCustomerId(rs.getInt("customer_id"));
        dto.setTotalInc(rs.getDouble("total_inc"));
        dto.setClosed(rs.getBoolean("closed"));
        dto.setComments(rs.getString("comments"));
        return dto;
    }

    // ---- Orders ----

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders ORDER BY order_id")) {
            while (rs.next()) list.add(mapOrderRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read orders from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<OrderDto> getOrderById(Integer orderId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE order_id = ?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapOrderRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read order from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        String sql = "INSERT INTO orders (revision, order_date, due_date, staff_id, supplier_id, order_suffix, comments, archive) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getRevision() != null ? order.getRevision() : 0);
            ps.setTimestamp(2, order.getOrderDate() != null ? Timestamp.valueOf(order.getOrderDate()) : null);
            ps.setTimestamp(3, order.getDueDate() != null ? Timestamp.valueOf(order.getDueDate()) : null);
            ps.setInt(4, order.getStaffId());
            ps.setInt(5, order.getSupplierId());
            ps.setString(6, order.getOrderSuffix());
            ps.setString(7, order.getComments());
            ps.setBoolean(8, order.getArchive() != null && order.getArchive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) order.setOrderId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert order into Postgres: " + e.getMessage(), e);
        }
        return order;
    }

    @Override
    public boolean updateOrder(OrderDto order) {
        String sql = "UPDATE orders SET revision = ?, order_date = ?, due_date = ?, staff_id = ?, " +
                "supplier_id = ?, order_suffix = ?, comments = ?, archive = ? WHERE order_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getRevision() != null ? order.getRevision() : 0);
            ps.setTimestamp(2, order.getOrderDate() != null ? Timestamp.valueOf(order.getOrderDate()) : null);
            ps.setTimestamp(3, order.getDueDate() != null ? Timestamp.valueOf(order.getDueDate()) : null);
            ps.setInt(4, order.getStaffId());
            ps.setInt(5, order.getSupplierId());
            ps.setString(6, order.getOrderSuffix());
            ps.setString(7, order.getComments());
            ps.setBoolean(8, order.getArchive() != null && order.getArchive());
            ps.setInt(9, order.getOrderId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteOrder(Integer orderId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE order_id = ?")) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order from Postgres: " + e.getMessage(), e);
        }
    }

    // ---- Layby orders ----

    @Override
    public List<LaybyOrderDto> getLaybyOrders() {
        List<LaybyOrderDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM layby ORDER BY layby_id")) {
            while (rs.next()) list.add(mapLaybyRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read layby orders from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<LaybyOrderDto> getLaybyOrderById(Integer laybyId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM layby WHERE layby_id = ?")) {
            ps.setInt(1, laybyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapLaybyRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read layby order from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public LaybyOrderDto createLaybyOrder(LaybyOrderDto layby) {
        String sql = "INSERT INTO layby (layby_date, customer_id, total_inc, closed, comments) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, layby.getLaybyDate() != null ? Timestamp.valueOf(layby.getLaybyDate()) : null);
            ps.setInt(2, layby.getCustomerId());
            ps.setDouble(3, layby.getTotalInc() != null ? layby.getTotalInc() : 0.0);
            ps.setBoolean(4, layby.getClosed() != null && layby.getClosed());
            ps.setString(5, layby.getComments());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) layby.setLaybyId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert layby order into Postgres: " + e.getMessage(), e);
        }
        return layby;
    }

    @Override
    public boolean updateLaybyOrder(LaybyOrderDto layby) {
        String sql = "UPDATE layby SET layby_date = ?, customer_id = ?, total_inc = ?, closed = ?, comments = ? WHERE layby_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, layby.getLaybyDate() != null ? Timestamp.valueOf(layby.getLaybyDate()) : null);
            ps.setInt(2, layby.getCustomerId());
            ps.setDouble(3, layby.getTotalInc() != null ? layby.getTotalInc() : 0.0);
            ps.setBoolean(4, layby.getClosed() != null && layby.getClosed());
            ps.setString(5, layby.getComments());
            ps.setInt(6, layby.getLaybyId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update layby order in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteLaybyOrder(Integer laybyId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM layby WHERE layby_id = ?")) {
            ps.setInt(1, laybyId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete layby order from Postgres: " + e.getMessage(), e);
        }
    }
}