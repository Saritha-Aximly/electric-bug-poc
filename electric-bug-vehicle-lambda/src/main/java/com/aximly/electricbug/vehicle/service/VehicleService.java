package com.aximly.electricbug.vehicle.service;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;

import java.util.List;

public interface VehicleService {
    List<VehicleMakeDto> getAllMakes();
    List<VehicleModelDto> getModelsForMake(String makeName);
}