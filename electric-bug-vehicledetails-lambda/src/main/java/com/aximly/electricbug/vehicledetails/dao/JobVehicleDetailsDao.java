package com.aximly.electricbug.vehicledetails.dao;

import com.aximly.electricbug.vehicledetails.dto.JobVehicleDetailsDto;

import java.util.Optional;

public interface JobVehicleDetailsDao {
    Optional<JobVehicleDetailsDto> getByJobId(Integer jobId);
    JobVehicleDetailsDto create(JobVehicleDetailsDto dto);
    boolean update(JobVehicleDetailsDto dto);
    boolean delete(Integer jobId);
}