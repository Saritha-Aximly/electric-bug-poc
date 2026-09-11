package com.aximly.electricbug.servicedetails.service;

import com.aximly.electricbug.servicedetails.dto.JobServiceDetailsDto;

import java.util.Optional;

public interface JobServiceDetailsService {
    Optional<JobServiceDetailsDto> getByJobId(Integer jobId);
    JobServiceDetailsDto create(JobServiceDetailsDto dto);
    boolean update(Integer jobId, JobServiceDetailsDto dto);
    boolean delete(Integer jobId);
}