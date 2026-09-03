package com.aximly.electricbug.marker.dao.impl;

import com.aximly.electricbug.marker.dao.JobInstallationMarkerDao;
import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobInstallationMarkerDaoImpl implements JobInstallationMarkerDao {

    private final DataSource cloudDataSource;

    public PostgresJobInstallationMarkerDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobInstallationMarkerDto mapRow(ResultSet rs) throws SQLException {
        JobInstallationMarkerDto dto = new JobInstallationMarkerDto();
        dto.setId(rs.getInt("id"));
        dto.setJobId(rs.getInt("job_id"));
        dto.setMarkerType(rs.getString("marker_type"));
        dto.setLabel(rs.getString("label"));
        dto.setNotes(rs.getString("notes"));
        Object x = rs.getObject("pos_x");
        Object y = rs.getObject("pos_y");
        dto.setPosX(x != null ? rs.getDouble("pos_x") : null);
        dto.setPosY(y != null ? rs.getDouble("pos_y") : null);
        return dto;
    }

    @Override
    public List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId) {
        List<JobInstallationMarkerDto> list = new ArrayList<>();
        String sql = "SELECT * FROM job_installation_markers WHERE job_id = ? ORDER BY id";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read markers from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobInstallationMarkerDto> getMarkerById(Integer id) {
        String sql = "SELECT * FROM job_installation_markers WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read marker from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker) {
        String sql = "INSERT INTO job_installation_markers (job_id, marker_type, label, notes, pos_x, pos_y) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, marker.getJobId());
            ps.setString(2, marker.getMarkerType());
            ps.setString(3, marker.getLabel());
            ps.setString(4, marker.getNotes());
            ps.setDouble(5, marker.getPosX());
            ps.setDouble(6, marker.getPosY());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) marker.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert marker into Postgres: " + e.getMessage(), e);
        }
        return marker;
    }

    @Override
    public boolean updateMarker(JobInstallationMarkerDto marker) {
        String sql = "UPDATE job_installation_markers SET marker_type = ?, label = ?, notes = ?, " +
                "pos_x = ?, pos_y = ? WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marker.getMarkerType());
            ps.setString(2, marker.getLabel());
            ps.setString(3, marker.getNotes());
            ps.setDouble(4, marker.getPosX());
            ps.setDouble(5, marker.getPosY());
            ps.setInt(6, marker.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update marker in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteMarker(Integer id) {
        String sql = "DELETE FROM job_installation_markers WHERE id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete marker from Postgres: " + e.getMessage(), e);
        }
    }
}