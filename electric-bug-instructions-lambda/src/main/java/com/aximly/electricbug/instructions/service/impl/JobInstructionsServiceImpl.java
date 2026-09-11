package com.aximly.electricbug.instructions.service.impl;

import com.aximly.electricbug.instructions.dao.JobInstructionsDao;
import com.aximly.electricbug.instructions.dto.JobInstructionsDto;
import com.aximly.electricbug.instructions.service.JobInstructionsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobInstructionsServiceImpl implements JobInstructionsService {

    private final JobInstructionsDao dao;

    public JobInstructionsServiceImpl(JobInstructionsDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<JobInstructionsDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public JobInstructionsDto create(JobInstructionsDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer jobId, JobInstructionsDto dto) {
        dto.setJobId(jobId);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer jobId) {
        return dao.delete(jobId);
    }
}