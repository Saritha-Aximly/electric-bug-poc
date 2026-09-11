package com.aximly.electricbug.serials.dao.impl;

import com.aximly.electricbug.serials.dao.JobSerialNumberDao;
import com.aximly.electricbug.serials.dto.JobSerialNumberDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobSerialNumberDaoImpl implements JobSerialNumberDao {

    private final DataSource cloudDataSource;

    public PostgresJobSerialNumberDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobSerialNumberDto mapRow(ResultSet rs) throws SQLException {
        JobSerialNumberDto dto = new JobSerialNumberDto();
        dto.setId(rs.getInt("id"));
        dto.setJobId(rs.getInt("job_id"));
        Object productId = rs.getObject("job_product_id");
        dto.setJobProductId(productId != null ? rs.getInt("job_product_id") : null);
        dto.setSerialNumber(rs.getString("serial_number"));
        return dto;
    }

    @Override
    public List<JobSerialNumberDto> getByJobId(Integer jobId) {
        List<JobSerialNumberDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_serial_numbers WHERE job_id = ? ORDER BY id")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read serial numbers from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<JobSerialNumberDto> getByProductId(Integer jobProductId) {
        List<JobSerialNumberDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_serial_numbers WHERE job_product_id = ? ORDER BY id")) {
            ps.setInt(1, jobProductId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read serial numbers from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobSerialNumberDto> getById(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_serial_numbers WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read serial number from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobSerialNumberDto create(JobSerialNumberDto dto) {
        String sql = "INSERT INTO job_serial_numbers (job_id, job_product_id, serial_number) VALUES (?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getJobId());
            if (dto.getJobProductId() != null) ps.setInt(2, dto.getJobProductId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, dto.getSerialNumber());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dto.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert serial number into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobSerialNumberDto dto) {
        String sql = "UPDATE job_serial_numbers SET job_product_id = ?, serial_number = ? WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (dto.getJobProductId() != null) ps.setInt(1, dto.getJobProductId()); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, dto.getSerialNumber());
            ps.setInt(3, dto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update serial number in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_serial_numbers WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete serial number from Postgres: " + e.getMessage(), e);
        }
    }
}