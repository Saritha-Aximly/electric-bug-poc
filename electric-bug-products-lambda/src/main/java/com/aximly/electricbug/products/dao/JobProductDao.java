package com.aximly.electricbug.products.dao;

import com.aximly.electricbug.products.dto.JobProductDto;

import java.util.List;
import java.util.Optional;

public interface JobProductDao {
    List<JobProductDto> getByJobId(Integer jobId);
    Optional<JobProductDto> getById(Integer id);
    JobProductDto create(JobProductDto dto);
    boolean update(JobProductDto dto);
    boolean delete(Integer id);
}