package com.aximly.electricbug.vehicledetails.service.impl;

import com.aximly.electricbug.vehicledetails.dao.JobVehicleDetailsDao;
import com.aximly.electricbug.vehicledetails.dto.JobVehicleDetailsDto;
import com.aximly.electricbug.vehicledetails.service.JobVehicleDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobVehicleDetailsServiceImpl implements JobVehicleDetailsService {

    private final JobVehicleDetailsDao dao;

    public JobVehicleDetailsServiceImpl(JobVehicleDetailsDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<JobVehicleDetailsDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public JobVehicleDetailsDto create(JobVehicleDetailsDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer jobId, JobVehicleDetailsDto dto) {
        dto.setJobId(jobId);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer jobId) {
        return dao.delete(jobId);
    }
}