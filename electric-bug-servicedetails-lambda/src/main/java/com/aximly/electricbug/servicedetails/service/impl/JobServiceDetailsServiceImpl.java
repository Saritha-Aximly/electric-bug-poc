package com.aximly.electricbug.servicedetails.service.impl;

import com.aximly.electricbug.servicedetails.dao.JobServiceDetailsDao;
import com.aximly.electricbug.servicedetails.dto.JobServiceDetailsDto;
import com.aximly.electricbug.servicedetails.service.JobServiceDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobServiceDetailsServiceImpl implements JobServiceDetailsService {

    private final JobServiceDetailsDao dao;

    public JobServiceDetailsServiceImpl(JobServiceDetailsDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<JobServiceDetailsDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public JobServiceDetailsDto create(JobServiceDetailsDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer jobId, JobServiceDetailsDto dto) {
        dto.setJobId(jobId);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer jobId) {
        return dao.delete(jobId);
    }
}