package com.aximly.electricbug.marker.controller;

import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;
import com.aximly.electricbug.marker.service.JobInstallationMarkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/markers")
public class JobInstallationMarkerController {

    private final JobInstallationMarkerService markerService;

    public JobInstallationMarkerController(JobInstallationMarkerService markerService) {
        this.markerService = markerService;
    }

    @GetMapping
    public ResponseEntity<?> getMarkers(@PathVariable Integer jobId) {
        return ResponseEntity.ok(markerService.getMarkersForJob(jobId));
    }

    @PostMapping
    public ResponseEntity<?> createMarker(@PathVariable Integer jobId, @RequestBody JobInstallationMarkerDto marker) {
        marker.setJobId(jobId);
        return ResponseEntity.ok(markerService.createMarker(marker));
    }

    @PutMapping("/{markerId}")
    public ResponseEntity<?> updateMarker(@PathVariable Integer jobId, @PathVariable Integer markerId,
                                           @RequestBody JobInstallationMarkerDto marker) {
        boolean updated = markerService.updateMarker(markerId, marker);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{markerId}")
    public ResponseEntity<?> deleteMarker(@PathVariable Integer jobId, @PathVariable Integer markerId) {
        boolean deleted = markerService.deleteMarker(markerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-marker-lambda"));
    }
}