package com.aximly.electricbug.vehicle.dao.impl;

import com.aximly.electricbug.vehicle.dao.VehicleDao;
import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostgresVehicleDaoImpl implements VehicleDao {

    private final DataSource cloudDataSource;

    public PostgresVehicleDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    // ---- Makes ----

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
    public VehicleMakeDto createMake(VehicleMakeDto make) {
        String sql = "INSERT INTO vehicle_makes (make_name) VALUES (?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, make.getMakeName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) make.setMakeId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert vehicle make into Postgres: " + e.getMessage(), e);
        }
        return make;
    }

    @Override
    public boolean updateMake(VehicleMakeDto make) {
        String sql = "UPDATE vehicle_makes SET make_name = ? WHERE make_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, make.getMakeName());
            ps.setInt(2, make.getMakeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vehicle make in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteMake(Integer makeId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM vehicle_makes WHERE make_id = ?")) {
            ps.setInt(1, makeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vehicle make from Postgres: " + e.getMessage(), e);
        }
    }

    // ---- Models ----

    @Override
    public List<VehicleModelDto> getModelsForMake(String makeName) {
        String sql = """
            SELECT vmo.model_id, vmo.make_id, vmo.model_name
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
                    results.add(new VehicleModelDto(
                            rs.getInt("model_id"), rs.getInt("make_id"), rs.getString("model_name")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read vehicle models from Postgres: " + e.getMessage(), e);
        }
        return results;
    }

    @Override
    public VehicleModelDto createModel(VehicleModelDto model) {
        String sql = "INSERT INTO vehicle_models (make_id, model_name) VALUES (?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, model.getMakeId());
            ps.setString(2, model.getModelName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) model.setModelId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert vehicle model into Postgres: " + e.getMessage(), e);
        }
        return model;
    }

    @Override
    public boolean updateModel(VehicleModelDto model) {
        String sql = "UPDATE vehicle_models SET make_id = ?, model_name = ? WHERE model_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, model.getMakeId());
            ps.setString(2, model.getModelName());
            ps.setInt(3, model.getModelId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vehicle model in Postgres: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteModel(Integer modelId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM vehicle_models WHERE model_id = ?")) {
            ps.setInt(1, modelId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vehicle model from Postgres: " + e.getMessage(), e);
        }
    }
}