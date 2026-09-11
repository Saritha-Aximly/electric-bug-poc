package com.aximly.electricbug.products.service;

import com.aximly.electricbug.products.dto.JobProductDto;

import java.util.List;
import java.util.Optional;

public interface JobProductService {
    List<JobProductDto> getByJobId(Integer jobId);
    Optional<JobProductDto> getById(Integer id);
    JobProductDto create(JobProductDto dto);
    boolean update(Integer id, JobProductDto dto);
    boolean delete(Integer id);
}