package com.aximly.electricbug.marker.dao;

import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;

import java.util.List;
import java.util.Optional;

public interface JobInstallationMarkerDao {
    List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId);
    Optional<JobInstallationMarkerDto> getMarker(Integer jobId, String markerId);
    JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker);
    boolean updateMarker(JobInstallationMarkerDto marker);
    boolean deleteMarker(Integer jobId, String markerId);
    void deleteAllForJob(Integer jobId);
}