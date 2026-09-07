package com.aximly.electricbug.vehicle.service.impl;

import com.aximly.electricbug.vehicle.dao.VehicleDao;
import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
import com.aximly.electricbug.vehicle.service.VehicleService;
import org.springframework.stereotype.Service;

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
    public VehicleMakeDto createMake(VehicleMakeDto make) {
        return vehicleDao.createMake(make);
    }

    @Override
    public boolean updateMake(Integer makeId, VehicleMakeDto make) {
        make.setMakeId(makeId);
        return vehicleDao.updateMake(make);
    }

    @Override
    public boolean deleteMake(Integer makeId) {
        return vehicleDao.deleteMake(makeId);
    }

    @Override
    public List<VehicleModelDto> getModelsForMake(String make) {
        return vehicleDao.getModelsForMake(make);
    }

    @Override
    public VehicleModelDto createModel(VehicleModelDto model) {
        return vehicleDao.createModel(model);
    }

    @Override
    public boolean updateModel(Integer modelId, VehicleModelDto model) {
        model.setModelId(modelId);
        return vehicleDao.updateModel(model);
    }

    @Override
    public boolean deleteModel(Integer modelId) {
        return vehicleDao.deleteModel(modelId);
    }
}