package com.aximly.electricbug.job.dao;

import com.aximly.electricbug.job.dto.JobSheetDto;

import java.util.List;
import java.util.Optional;

public interface JobSheetDao {
    List<JobSheetDto> getAllJobs();
    Optional<JobSheetDto> getJobById(Integer jobId);
    List<JobSheetDto> getJobsByStatus(String status);
    JobSheetDto createJob(JobSheetDto job);
    boolean updateJob(JobSheetDto job);
    boolean deleteJob(Integer jobId);
}