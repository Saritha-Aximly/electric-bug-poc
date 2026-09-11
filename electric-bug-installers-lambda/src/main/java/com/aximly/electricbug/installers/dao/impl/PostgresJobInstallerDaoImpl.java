package com.aximly.electricbug.installers.dao.impl;

import com.aximly.electricbug.installers.dao.JobInstallerDao;
import com.aximly.electricbug.installers.dto.JobInstallerDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobInstallerDaoImpl implements JobInstallerDao {

    private final DataSource cloudDataSource;

    public PostgresJobInstallerDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobInstallerDto mapRow(ResultSet rs) throws SQLException {
        JobInstallerDto dto = new JobInstallerDto();
        dto.setId(rs.getInt("id"));
        dto.setJobId(rs.getInt("job_id"));
        dto.setStaffId(rs.getInt("staff_id"));
        Object hours = rs.getObject("hours");
        dto.setHours(hours != null ? rs.getDouble("hours") : null);
        return dto;
    }

    @Override
    public List<JobInstallerDto> getByJobId(Integer jobId) {
        List<JobInstallerDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_installers WHERE job_id = ? ORDER BY id")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read installers from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobInstallerDto> getById(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_installers WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read installer from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobInstallerDto create(JobInstallerDto dto) {
        String sql = "INSERT INTO job_installers (job_id, staff_id, hours) VALUES (?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getJobId());
            ps.setInt(2, dto.getStaffId());
            if (dto.getHours() != null) ps.setDouble(3, dto.getHours()); else ps.setNull(3, Types.DOUBLE);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dto.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert installer into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobInstallerDto dto) {
        String sql = "UPDATE job_installers SET staff_id = ?, hours = ? WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getStaffId());
            if (dto.getHours() != null) ps.setDouble(2, dto.getHours()); else ps.setNull(2, Types.DOUBLE);
            ps.setInt(3, dto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update installer in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_installers WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete installer from Postgres: " + e.getMessage(), e);
        }
    }
}