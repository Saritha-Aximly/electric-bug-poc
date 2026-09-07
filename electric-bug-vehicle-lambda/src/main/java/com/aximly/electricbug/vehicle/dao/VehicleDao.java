package com.aximly.electricbug.vehicle.dao;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;

import java.util.List;

public interface VehicleDao {
    List<VehicleMakeDto> getAllMakes();
    VehicleMakeDto createMake(VehicleMakeDto make);
    boolean updateMake(VehicleMakeDto make);
    boolean deleteMake(Integer makeId);

    List<VehicleModelDto> getModelsForMake(String makeName);
    VehicleModelDto createModel(VehicleModelDto model);
    boolean updateModel(VehicleModelDto model);
    boolean deleteModel(Integer modelId);
}