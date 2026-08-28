package com.aximly.electricbug.vehicle.dao;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;

import java.util.List;

public interface VehicleDao {

    List<VehicleMakeDto> getAllMakes();

    List<VehicleModelDto> getModelsForMake(String makeName);
}