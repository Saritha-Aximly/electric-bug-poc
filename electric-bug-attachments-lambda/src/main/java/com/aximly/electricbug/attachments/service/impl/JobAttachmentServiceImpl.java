package com.aximly.electricbug.attachments.service.impl;

import com.aximly.electricbug.attachments.dao.JobAttachmentDao;
import com.aximly.electricbug.attachments.dto.JobAttachmentDto;
import com.aximly.electricbug.attachments.service.JobAttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobAttachmentServiceImpl implements JobAttachmentService {

    private final JobAttachmentDao dao;

    public JobAttachmentServiceImpl(JobAttachmentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<JobAttachmentDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public List<JobAttachmentDto> getByJobIdAndSection(Integer jobId, String section) {
        return dao.getByJobIdAndSection(jobId, section);
    }

    @Override
    public Optional<JobAttachmentDto> getById(Integer id) {
        return dao.getById(id);
    }

    @Override
    public JobAttachmentDto create(JobAttachmentDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer id, JobAttachmentDto dto) {
        dto.setId(id);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer id) {
        return dao.delete(id);
    }
}