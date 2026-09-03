package com.aximly.electricbug.marker.service.impl;

import com.aximly.electricbug.marker.dao.JobInstallationMarkerDao;
import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;
import com.aximly.electricbug.marker.service.JobInstallationMarkerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobInstallationMarkerServiceImpl implements JobInstallationMarkerService {

    private final JobInstallationMarkerDao markerDao;

    public JobInstallationMarkerServiceImpl(JobInstallationMarkerDao markerDao) {
        this.markerDao = markerDao;
    }

    @Override
    public List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId) {
        return markerDao.getMarkersForJob(jobId);
    }

    @Override
    public Optional<JobInstallationMarkerDto> getMarkerById(Integer id) {
        return markerDao.getMarkerById(id);
    }

    @Override
    public JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker) {
        return markerDao.createMarker(marker);
    }

    @Override
    public boolean updateMarker(Integer id, JobInstallationMarkerDto marker) {
        marker.setId(id);
        return markerDao.updateMarker(marker);
    }

    @Override
    public boolean deleteMarker(Integer id) {
        return markerDao.deleteMarker(id);
    }
}