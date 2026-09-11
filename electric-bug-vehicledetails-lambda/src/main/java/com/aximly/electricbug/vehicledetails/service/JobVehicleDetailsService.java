package com.aximly.electricbug.vehicledetails.service;

import com.aximly.electricbug.vehicledetails.dto.JobVehicleDetailsDto;

import java.util.Optional;

public interface JobVehicleDetailsService {
    Optional<JobVehicleDetailsDto> getByJobId(Integer jobId);
    JobVehicleDetailsDto create(JobVehicleDetailsDto dto);
    boolean update(Integer jobId, JobVehicleDetailsDto dto);
    boolean delete(Integer jobId);
}