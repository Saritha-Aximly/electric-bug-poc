package com.aximly.electricbug.attachments.service;

import com.aximly.electricbug.attachments.dto.JobAttachmentDto;

import java.util.List;
import java.util.Optional;

public interface JobAttachmentService {
    List<JobAttachmentDto> getByJobId(Integer jobId);
    List<JobAttachmentDto> getByJobIdAndSection(Integer jobId, String section);
    Optional<JobAttachmentDto> getById(Integer id);
    JobAttachmentDto create(JobAttachmentDto dto);
    boolean update(Integer id, JobAttachmentDto dto);
    boolean delete(Integer id);
}