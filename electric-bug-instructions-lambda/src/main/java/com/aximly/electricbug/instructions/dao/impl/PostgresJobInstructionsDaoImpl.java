package com.aximly.electricbug.instructions.dao.impl;

import com.aximly.electricbug.instructions.dao.JobInstructionsDao;
import com.aximly.electricbug.instructions.dto.JobInstructionsDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class PostgresJobInstructionsDaoImpl implements JobInstructionsDao {

    private final DataSource cloudDataSource;

    public PostgresJobInstructionsDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobInstructionsDto mapRow(ResultSet rs) throws SQLException {
        JobInstructionsDto dto = new JobInstructionsDto();
        dto.setJobId(rs.getInt("job_id"));
        dto.setInstructionsText(rs.getString("instructions_text"));
        return dto;
    }

    @Override
    public Optional<JobInstructionsDto> getByJobId(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_instructions WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read instructions from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobInstructionsDto create(JobInstructionsDto dto) {
        String sql = "INSERT INTO job_instructions (job_id, instructions_text) VALUES (?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getJobId());
            ps.setString(2, dto.getInstructionsText());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert instructions into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobInstructionsDto dto) {
        String sql = "UPDATE job_instructions SET instructions_text = ? WHERE job_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getInstructionsText());
            ps.setInt(2, dto.getJobId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update instructions in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_instructions WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete instructions from Postgres: " + e.getMessage(), e);
        }
    }
}