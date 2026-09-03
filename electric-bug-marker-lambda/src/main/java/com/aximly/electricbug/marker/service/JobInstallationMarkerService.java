package com.aximly.electricbug.marker.service;

import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;

import java.util.List;
import java.util.Optional;

public interface JobInstallationMarkerService {
    List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId);
    Optional<JobInstallationMarkerDto> getMarkerById(Integer id);
    JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker);
    boolean updateMarker(Integer id, JobInstallationMarkerDto marker);
    boolean deleteMarker(Integer id);
}