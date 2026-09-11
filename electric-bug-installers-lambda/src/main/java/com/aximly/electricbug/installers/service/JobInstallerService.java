package com.aximly.electricbug.installers.service;

import com.aximly.electricbug.installers.dto.JobInstallerDto;

import java.util.List;
import java.util.Optional;

public interface JobInstallerService {
    List<JobInstallerDto> getByJobId(Integer jobId);
    Optional<JobInstallerDto> getById(Integer id);
    JobInstallerDto create(JobInstallerDto dto);
    boolean update(Integer id, JobInstallerDto dto);
    boolean delete(Integer id);
}