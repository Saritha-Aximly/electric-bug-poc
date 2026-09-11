package com.aximly.electricbug.installers.service.impl;

import com.aximly.electricbug.installers.dao.JobInstallerDao;
import com.aximly.electricbug.installers.dto.JobInstallerDto;
import com.aximly.electricbug.installers.service.JobInstallerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobInstallerServiceImpl implements JobInstallerService {

    private final JobInstallerDao dao;

    public JobInstallerServiceImpl(JobInstallerDao dao) {
        this.dao = dao;
    }

    @Override
    public List<JobInstallerDto> getByJobId(Integer jobId) {
        return dao.getByJobId(jobId);
    }

    @Override
    public Optional<JobInstallerDto> getById(Integer id) {
        return dao.getById(id);
    }

    @Override
    public JobInstallerDto create(JobInstallerDto dto) {
        return dao.create(dto);
    }

    @Override
    public boolean update(Integer id, JobInstallerDto dto) {
        dto.setId(id);
        return dao.update(dto);
    }

    @Override
    public boolean delete(Integer id) {
        return dao.delete(id);
    }
}