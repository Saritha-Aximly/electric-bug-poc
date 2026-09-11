package com.aximly.electricbug.servicedetails.dao;

import com.aximly.electricbug.servicedetails.dto.JobServiceDetailsDto;

import java.util.Optional;

public interface JobServiceDetailsDao {
    Optional<JobServiceDetailsDto> getByJobId(Integer jobId);
    JobServiceDetailsDto create(JobServiceDetailsDto dto);
    boolean update(JobServiceDetailsDto dto);
    boolean delete(Integer jobId);
}