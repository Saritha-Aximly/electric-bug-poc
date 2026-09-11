package com.aximly.electricbug.instructions.service;

import com.aximly.electricbug.instructions.dto.JobInstructionsDto;

import java.util.Optional;

public interface JobInstructionsService {
    Optional<JobInstructionsDto> getByJobId(Integer jobId);
    JobInstructionsDto create(JobInstructionsDto dto);
    boolean update(Integer jobId, JobInstructionsDto dto);
    boolean delete(Integer jobId);
}