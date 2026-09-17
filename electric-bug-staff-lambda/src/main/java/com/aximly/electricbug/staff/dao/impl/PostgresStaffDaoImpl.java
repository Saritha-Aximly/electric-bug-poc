package com.aximly.electricbug.staff.dao.impl;

import com.aximly.electricbug.staff.dao.StaffDao;
import com.aximly.electricbug.staff.dto.StaffDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresStaffDaoImpl implements StaffDao {

    private final DataSource cloudDataSource;

    public PostgresStaffDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private StaffDto mapRow(ResultSet rs) throws SQLException {
        StaffDto dto = new StaffDto();
        dto.setStaffId(rs.getInt("staff_id"));
        dto.setFullName(rs.getString("full_name"));
        dto.setRole(rs.getString("role"));
        dto.setActive(rs.getBoolean("active"));
        return dto;
    }

    @Override
    public List<StaffDto> getAllStaff(boolean activeOnly) {
        List<StaffDto> list = new ArrayList<>();
        String sql = activeOnly
                ? "SELECT * FROM staff WHERE active = true ORDER BY full_name"
                : "SELECT * FROM staff ORDER BY full_name";
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read staff from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<StaffDto> getStaffByRole(String role, boolean activeOnly) {
        List<StaffDto> list = new ArrayList<>();
        String sql = activeOnly
                ? "SELECT * FROM staff WHERE role = ? AND active = true ORDER BY full_name"
                : "SELECT * FROM staff WHERE role = ? ORDER BY full_name";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read staff by role from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<StaffDto> getStaffById(Integer staffId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM staff WHERE staff_id = ?")) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read staff member from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public StaffDto createStaff(StaffDto staff) {
        String sql = "INSERT INTO staff (full_name, role, active) VALUES (?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getRole());
            ps.setBoolean(3, staff.getActive() != null ? staff.getActive() : true);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) staff.setStaffId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert staff member into Postgres: " + e.getMessage(), e);
        }
        return staff;
    }

    @Override
    public boolean updateStaff(StaffDto staff) {
        String sql = "UPDATE staff SET full_name = ?, role = ?, active = ? WHERE staff_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getRole());
            ps.setBoolean(3, staff.getActive() != null ? staff.getActive() : true);
            ps.setInt(4, staff.getStaffId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update staff member in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deactivateStaff(Integer staffId) {
        String sql = "UPDATE staff SET active = false WHERE staff_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deactivate staff member in Postgres: " + e.getMessage(), e);
        }
    }
}