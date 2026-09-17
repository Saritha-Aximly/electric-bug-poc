package com.aximly.electricbug.marker.service;

import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;

import java.util.List;
import java.util.Optional;

public interface JobInstallationMarkerService {
    List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId);
    Optional<JobInstallationMarkerDto> getMarker(Integer jobId, String markerId);
    JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker);
    boolean updateMarker(Integer jobId, String markerId, JobInstallationMarkerDto marker);
    boolean deleteMarker(Integer jobId, String markerId);
    void deleteAllForJob(Integer jobId);
}