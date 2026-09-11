package com.aximly.electricbug.products.service.impl;

import com.aximly.electricbug.products.dao.JobProductDao;
import com.aximly.electricbug.products.dto.JobProductDto;
import com.aximly.electricbug.products.service.JobProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobProductServiceImpl implements JobProductService {

    private final JobProductDao dao;

    public JobProductServiceImpl(JobProductDao dao) {
        this.dao = dao;
    }

    @Override
    public List<JobProductDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public Optional<JobProductDto> getById(Integer id) {
        return dao.getById(id);
    }

    @Override
    public JobProductDto create(JobProductDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer id, JobProductDto dto) {
        dto.setId(id);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer id) {
        return dao.delete(id);
    }
}