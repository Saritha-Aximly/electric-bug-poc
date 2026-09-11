package com.aximly.electricbug.attachments.dao.impl;

import com.aximly.electricbug.attachments.dao.JobAttachmentDao;
import com.aximly.electricbug.attachments.dto.JobAttachmentDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobAttachmentDaoImpl implements JobAttachmentDao {

    private final DataSource cloudDataSource;

    public PostgresJobAttachmentDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobAttachmentDto mapRow(ResultSet rs) throws SQLException {
        JobAttachmentDto dto = new JobAttachmentDto();
        dto.setId(rs.getInt("id"));
        dto.setJobId(rs.getInt("job_id"));
        dto.setSection(rs.getString("section"));
        dto.setFileUrl(rs.getString("file_url"));
        Object size = rs.getObject("file_size_bytes");
        dto.setFileSizeBytes(size != null ? rs.getLong("file_size_bytes") : null);
        dto.setNotes(rs.getString("notes"));
        Timestamp uploadedAt = rs.getTimestamp("uploaded_at");
        dto.setUploadedAt(uploadedAt != null ? uploadedAt.toInstant().atOffset(ZoneOffset.UTC) : null);
        return dto;
    }

    @Override
    public List<JobAttachmentDto> getByJobId(Integer jobId) {
        List<JobAttachmentDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_attachments WHERE job_id = ? ORDER BY id")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read attachments from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<JobAttachmentDto> getByJobIdAndSection(Integer jobId, String section) {
        List<JobAttachmentDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM job_attachments WHERE job_id = ? AND section = ? ORDER BY id")) {
            ps.setInt(1, jobId);
            ps.setString(2, section);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read attachments from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobAttachmentDto> getById(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_attachments WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read attachment from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobAttachmentDto create(JobAttachmentDto dto) {
        String sql = "INSERT INTO job_attachments (job_id, section, file_url, file_size_bytes, notes) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getJobId());
            ps.setString(2, dto.getSection());
            ps.setString(3, dto.getFileUrl());
            if (dto.getFileSizeBytes() != null) ps.setLong(4, dto.getFileSizeBytes()); else ps.setNull(4, Types.BIGINT);
            ps.setString(5, dto.getNotes());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dto.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert attachment into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobAttachmentDto dto) {
        String sql = "UPDATE job_attachments SET section = ?, file_url = ?, file_size_bytes = ?, notes = ? WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getSection());
            ps.setString(2, dto.getFileUrl());
            if (dto.getFileSizeBytes() != null) ps.setLong(3, dto.getFileSizeBytes()); else ps.setNull(3, Types.BIGINT);
            ps.setString(4, dto.getNotes());
            ps.setInt(5, dto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update attachment in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_attachments WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete attachment from Postgres: " + e.getMessage(), e);
        }
    }
}