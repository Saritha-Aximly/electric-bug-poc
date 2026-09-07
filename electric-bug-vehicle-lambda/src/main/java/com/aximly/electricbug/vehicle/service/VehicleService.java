package com.aximly.electricbug.vehicle.service;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;

import java.util.List;

public interface VehicleService {
    List<VehicleMakeDto> getAllMakes();
    VehicleMakeDto createMake(VehicleMakeDto make);
    boolean updateMake(Integer makeId, VehicleMakeDto make);
    boolean deleteMake(Integer makeId);

    List<VehicleModelDto> getModelsForMake(String make);
    VehicleModelDto createModel(VehicleModelDto model);
    boolean updateModel(Integer modelId, VehicleModelDto model);
    boolean deleteModel(Integer modelId);
}