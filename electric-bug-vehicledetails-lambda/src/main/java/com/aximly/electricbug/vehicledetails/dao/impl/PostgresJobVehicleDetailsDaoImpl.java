package com.aximly.electricbug.vehicledetails.dao.impl;

import com.aximly.electricbug.vehicledetails.dao.JobVehicleDetailsDao;
import com.aximly.electricbug.vehicledetails.dto.JobVehicleDetailsDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class PostgresJobVehicleDetailsDaoImpl implements JobVehicleDetailsDao {

    private final DataSource cloudDataSource;

    public PostgresJobVehicleDetailsDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobVehicleDetailsDto mapRow(ResultSet rs) throws SQLException {
        JobVehicleDetailsDto dto = new JobVehicleDetailsDto();
        dto.setJobId(rs.getInt("job_id"));
        dto.setMake(rs.getString("make"));
        dto.setModel(rs.getString("model"));
        Object year = rs.getObject("year");
        dto.setYear(year != null ? rs.getInt("year") : null);
        dto.setRegistration(rs.getString("registration"));
        dto.setArrivalPhotoUrl(rs.getString("arrival_photo_url"));
        return dto;
    }

    @Override
    public Optional<JobVehicleDetailsDto> getByJobId(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_vehicle_details WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read vehicle details from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public JobVehicleDetailsDto create(JobVehicleDetailsDto dto) {
        String sql = "INSERT INTO job_vehicle_details (job_id, make, model, year, registration, arrival_photo_url) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getJobId());
            ps.setString(2, dto.getMake());
            ps.setString(3, dto.getModel());
            if (dto.getYear() != null) ps.setInt(4, dto.getYear()); else ps.setNull(4, Types.SMALLINT);
            ps.setString(5, dto.getRegistration());
            ps.setString(6, dto.getArrivalPhotoUrl());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert vehicle details into Postgres: " + e.getMessage(), e);
        }
        return dto;
    }

    @Override
    public boolean update(JobVehicleDetailsDto dto) {
        String sql = "UPDATE job_vehicle_details SET make = ?, model = ?, year = ?, registration = ?, " +
                "arrival_photo_url = ? WHERE job_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getMake());
            ps.setString(2, dto.getModel());
            if (dto.getYear() != null) ps.setInt(3, dto.getYear()); else ps.setNull(3, Types.SMALLINT);
            ps.setString(4, dto.getRegistration());
            ps.setString(5, dto.getArrivalPhotoUrl());
            ps.setInt(6, dto.getJobId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vehicle details in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_vehicle_details WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vehicle details from Postgres: " + e.getMessage(), e);
        }
    }
}