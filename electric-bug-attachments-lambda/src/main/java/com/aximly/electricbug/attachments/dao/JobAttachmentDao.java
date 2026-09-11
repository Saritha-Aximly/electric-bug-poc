package com.aximly.electricbug.attachments.dao;

import com.aximly.electricbug.attachments.dto.JobAttachmentDto;

import java.util.List;
import java.util.Optional;

public interface JobAttachmentDao {
    List<JobAttachmentDto> getByJobId(Integer jobId);
    List<JobAttachmentDto> getByJobIdAndSection(Integer jobId, String section);
    Optional<JobAttachmentDto> getById(Integer id);
    JobAttachmentDto create(JobAttachmentDto dto);
    boolean update(JobAttachmentDto dto);
    boolean delete(Integer id);
}