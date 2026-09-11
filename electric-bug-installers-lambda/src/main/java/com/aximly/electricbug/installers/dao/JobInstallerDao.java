package com.aximly.electricbug.installers.dao;

import com.aximly.electricbug.installers.dto.JobInstallerDto;

import java.util.List;
import java.util.Optional;

public interface JobInstallerDao {
    List<JobInstallerDto> getByJobId(Integer jobId);
    Optional<JobInstallerDto> getById(Integer id);
    JobInstallerDto create(JobInstallerDto dto);
    boolean update(JobInstallerDto dto);
    boolean delete(Integer id);
}