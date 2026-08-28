package com.aximly.electricbug.vehicle.service.impl;

import com.aximly.electricbug.vehicle.dao.VehicleDao;
import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
import org.springframework.stereotype.Service;
import com.aximly.electricbug.vehicle.service.VehicleService;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleDao vehicleDao;

    public VehicleServiceImpl(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    @Override
    public List<VehicleMakeDto> getAllMakes() {
        return vehicleDao.getAllMakes();
    }

    @Override
    public List<VehicleModelDto> getModelsForMake(String makeName) {
        return vehicleDao.getModelsForMake(makeName);
    }
}