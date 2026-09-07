package com.aximly.electricbug.customer.dao.impl;

import com.aximly.electricbug.customer.dao.CustomerDao;
import com.aximly.electricbug.customer.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "externalApiFlag", havingValue = "false", matchIfMissing = true)
public class PostgresCustomerDaoImpl implements CustomerDao {

    private final DataSource cloudDataSource;

    public PostgresCustomerDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private CustomerDto mapRow(ResultSet rs) throws SQLException {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(rs.getInt("customer_id"));
        dto.setGivenNames(rs.getString("given_names"));
        dto.setSurname(rs.getString("surname"));
        dto.setEmail(rs.getString("email"));
        dto.setPhone(rs.getString("phone"));
        return dto;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer ORDER BY customer_id")) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read customers from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public CustomerDto getCustomerById(int id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE customer_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read customer " + id + " from Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customer) {
        String sql = "INSERT INTO customer (given_names, surname, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getGivenNames());
            ps.setString(2, customer.getSurname());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) customer.setCustomerId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert customer into Postgres: " + e.getMessage(), e);
        }
        return customer;
    }

    @Override
    public boolean updateCustomer(CustomerDto customer) {
        String sql = "UPDATE customer SET given_names = ?, surname = ?, email = ?, phone = ? WHERE customer_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getGivenNames());
            ps.setString(2, customer.getSurname());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteCustomer(int id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM customer WHERE customer_id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete customer from Postgres: " + e.getMessage(), e);
        }
    }
}