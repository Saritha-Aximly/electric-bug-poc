package com.aximly.electricbug.job.service.impl;

import com.aximly.electricbug.job.dao.JobSheetDao;
import com.aximly.electricbug.job.dto.JobSheetDto;
import com.aximly.electricbug.job.service.JobSheetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobSheetServiceImpl implements JobSheetService {

    private final JobSheetDao jobSheetDao;

    public JobSheetServiceImpl(JobSheetDao jobSheetDao) {
        this.jobSheetDao = jobSheetDao;
    }

    @Override
    public List<JobSheetDto> getAllJobs() {
        return jobSheetDao.getAllJobs();
    }

    @Override
    public Optional<JobSheetDto> getJobById(Integer jobId) {
        return jobSheetDao.getJobById(jobId);
    }

    @Override
    public List<JobSheetDto> getJobsByStatus(String status) {
        return jobSheetDao.getJobsByStatus(status);
    }

    @Override
    public JobSheetDto createJob(JobSheetDto job) {
        return jobSheetDao.createJob(job);
    }

    @Override
    public boolean updateJob(Integer jobId, JobSheetDto job) {
        job.setJobId(jobId);
        return jobSheetDao.updateJob(job);
    }

    @Override
    public boolean deleteJob(Integer jobId) {
        return jobSheetDao.deleteJob(jobId);
    }
}