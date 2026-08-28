package com.aximly.electricbug.vehicle.dao.impl;

import com.aximly.electricbug.vehicle.dao.VehicleDao;
import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
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

@Repository
public class PostgresVehicleDaoImpl implements VehicleDao {

    private final DataSource cloudDataSource;

    public PostgresVehicleDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    @Override
    public List<VehicleMakeDto> getAllMakes() {
        String sql = "SELECT make_id, make_name FROM vehicle_makes ORDER BY make_name";
        List<VehicleMakeDto> results = new ArrayList<>();

        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new VehicleMakeDto(rs.getInt("make_id"), rs.getString("make_name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read vehicle makes from Postgres: " + e.getMessage(), e);
        }
        return results;
    }

    @Override
    public List<VehicleModelDto> getModelsForMake(String makeName) {
        String sql = """
            SELECT vmo.model_id, vmo.model_name
            FROM vehicle_models vmo
            JOIN vehicle_makes vma ON vmo.make_id = vma.make_id
            WHERE LOWER(vma.make_name) = LOWER(?)
            ORDER BY vmo.model_name
            """;
        List<VehicleModelDto> results = new ArrayList<>();

        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, makeName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new VehicleModelDto(rs.getInt("model_id"), rs.getString("model_name")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read vehicle models from Postgres: " + e.getMessage(), e);
        }
        return results;
    }
}