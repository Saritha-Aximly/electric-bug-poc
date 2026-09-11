package com.aximly.electricbug.serials.service.impl;

import com.aximly.electricbug.serials.dao.JobSerialNumberDao;
import com.aximly.electricbug.serials.dto.JobSerialNumberDto;
import com.aximly.electricbug.serials.service.JobSerialNumberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobSerialNumberServiceImpl implements JobSerialNumberService {

    private final JobSerialNumberDao dao;

    public JobSerialNumberServiceImpl(JobSerialNumberDao dao) {
        this.dao = dao;
    }

    @Override
    public List<JobSerialNumberDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public List<JobSerialNumberDto> getByProductId(Integer jobProductId) {
        return dao.getByProductId(jobProductId);
    }

    @Override
    public Optional<JobSerialNumberDto> getById(Integer id) {
        return dao.getById(id);
    }

    @Override
    public JobSerialNumberDto create(JobSerialNumberDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer id, JobSerialNumberDto dto) {
        dto.setId(id);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer id) {
        return dao.delete(id);
    }
}