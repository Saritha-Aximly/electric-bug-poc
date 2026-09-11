package com.aximly.electricbug.serials.dao;

import com.aximly.electricbug.serials.dto.JobSerialNumberDto;

import java.util.List;
import java.util.Optional;

public interface JobSerialNumberDao {
    List<JobSerialNumberDto> getByJobId(Integer jobId);
    List<JobSerialNumberDto> getByProductId(Integer jobProductId);
    Optional<JobSerialNumberDto> getById(Integer id);
    JobSerialNumberDto create(JobSerialNumberDto dto);
    boolean update(JobSerialNumberDto dto);
    boolean delete(Integer id);
}