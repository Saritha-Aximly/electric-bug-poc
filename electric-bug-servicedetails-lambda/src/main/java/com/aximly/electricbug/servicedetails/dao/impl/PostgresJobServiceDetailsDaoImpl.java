package com.aximly.electricbug.servicedetails.dao.impl;

import com.aximly.electricbug.servicedetails.dao.JobServiceDetailsDao;
import com.aximly.electricbug.servicedetails.dto.JobServiceDetailsDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class PostgresJobServiceDetailsDaoImpl implements JobServiceDetailsDao {

    private final DataSource cloudDataSource;

    public PostgresJobServiceDetailsDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobServiceDetailsDto mapRow(ResultSet rs) throws SQLException {
        JobServiceDetailsDto dto = new JobServiceDetailsDto();
        dto.setJobId(rs.getInt("job_id"));
        Date serviceDate = rs.getDate("service_date");
        dto.setServiceDate(serviceDate != null ? serviceDate.toLocalDate() : null);
        dto.setJobInTime(rs.getString("job_in_time"));
        dto.setPickupTime(rs.getString("pickup_time"));
        Object estHours = rs.getObject("estimated_hours");
        dto.setEstimatedHours(estHours != null ? rs.getDouble("estimated_hours") : null);
        Object bookingRepId = rs.getObject("booking_rep_id");
        dto.setBookingRepId(bookingRepId != null ? rs.getInt("booking_rep_id") : null);
        Object plannerId = rs.getObject("planner_id");
        dto.setPlannerId(plannerId != null ? rs.getInt("planner_id") : null);
        dto.setLocation(rs.getString("location"));
        return dto;
    }

    @Override
    public Optional<JobServiceDetailsDto> getByJobId(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_service_details WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read service details from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobServiceDetailsDto create(JobServiceDetailsDto dto) {
        String sql = "INSERT INTO job_service_details (job_id, service_date, job_in_time, pickup_time, " +
                "estimated_hours, booking_rep_id, planner_id, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, dto, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert service details into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobServiceDetailsDto dto) {
        String sql = "UPDATE job_service_details SET service_date = ?, job_in_time = ?, pickup_time = ?, " +
                "estimated_hours = ?, booking_rep_id = ?, planner_id = ?, location = ? WHERE job_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = bindParams(ps, dto, true);
            ps.setInt(i, dto.getJobId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update service details in Postgres: " + e.getMessage(), e);
        }
    }

    private int bindParams(PreparedStatement ps, JobServiceDetailsDto dto, boolean skipJobId) throws SQLException {
        int i = 1;
        if (!skipJobId) ps.setInt(i++, dto.getJobId());
        if (dto.getServiceDate() != null) ps.setDate(i++, Date.valueOf(dto.getServiceDate())); else ps.setNull(i++, Types.DATE);
        ps.setString(i++, dto.getJobInTime());
        ps.setString(i++, dto.getPickupTime());
        if (dto.getEstimatedHours() != null) ps.setDouble(i++, dto.getEstimatedHours()); else ps.setNull(i++, Types.DOUBLE);
        if (dto.getBookingRepId() != null) ps.setInt(i++, dto.getBookingRepId()); else ps.setNull(i++, Types.INTEGER);
        if (dto.getPlannerId() != null) ps.setInt(i++, dto.getPlannerId()); else ps.setNull(i++, Types.INTEGER);
        ps.setString(i++, dto.getLocation());
        return i;
    }

    @Override
    public boolean delete(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_service_details WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete service details from Postgres: " + e.getMessage(), e);
        }
    }
}