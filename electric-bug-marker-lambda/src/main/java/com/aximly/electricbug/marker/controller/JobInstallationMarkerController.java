package com.aximly.electricbug.marker.controller;

import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;
import com.aximly.electricbug.marker.service.JobInstallationMarkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/markers")
@Tag(name = "Markers", description = "Manage installation markers placed on a job's vehicle diagram")
public class JobInstallationMarkerController {

    private final JobInstallationMarkerService markerService;

    public JobInstallationMarkerController(JobInstallationMarkerService markerService) {
        this.markerService = markerService;
    }

    @GetMapping
    @Operation(summary = "Get all markers for a job")
    public ResponseEntity<?> getMarkers(@PathVariable Integer jobId) {
        return ResponseEntity.ok(markerService.getMarkersForJob(jobId));
    }

    @PostMapping
    @Operation(summary = "Place a new marker on a job's diagram")
    public ResponseEntity<?> createMarker(@PathVariable Integer jobId, @RequestBody JobInstallationMarkerDto marker) {
        marker.setJobId(jobId);
        return ResponseEntity.ok(markerService.createMarker(marker));
    }

    @PutMapping("/{markerId}")
    @Operation(summary = "Edit a specific marker on a job's diagram")
    public ResponseEntity<?> updateMarker(@PathVariable Integer jobId, @PathVariable Integer markerId,
                                           @RequestBody JobInstallationMarkerDto marker) {
        boolean updated = markerService.updateMarker(markerId, marker);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{markerId}")
    @Operation(summary = "Delete a specific marker on a job's diagram")
    public ResponseEntity<?> deleteMarker(@PathVariable Integer jobId, @PathVariable Integer markerId) {
        boolean deleted = markerService.deleteMarker(markerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check on marker on job's diagram")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-marker-lambda"));
    }
}