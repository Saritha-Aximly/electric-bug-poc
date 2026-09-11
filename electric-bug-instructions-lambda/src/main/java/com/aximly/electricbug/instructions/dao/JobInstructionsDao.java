package com.aximly.electricbug.instructions.dao;

import com.aximly.electricbug.instructions.dto.JobInstructionsDto;

import java.util.Optional;

public interface JobInstructionsDao {
    Optional<JobInstructionsDto> getByJobId(Integer jobId);
    JobInstructionsDto create(JobInstructionsDto dto);
    boolean update(JobInstructionsDto dto);
    boolean delete(Integer jobId);
}