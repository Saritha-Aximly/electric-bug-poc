package com.aximly.electricbug.servicedetails.controller;

import com.aximly.electricbug.servicedetails.dto.JobServiceDetailsDto;
import com.aximly.electricbug.servicedetails.service.JobServiceDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/service-details")
@Tag(name = "Job Service Details", description = "Service details for a job (Section 3 of the job sheet)")
public class JobServiceDetailsController {

    private final JobServiceDetailsService service;

    public JobServiceDetailsController(JobServiceDetailsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get service details for a job")
    public ResponseEntity<?> get(@PathVariable Integer jobId) {
        return service.getByJobId(jobId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create service details for a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobServiceDetailsDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping
    @Operation(summary = "Update service details for a job")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @RequestBody JobServiceDetailsDto dto) {
        boolean updated = service.update(jobId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete service details for a job")
    public ResponseEntity<?> delete(@PathVariable Integer jobId) {
        boolean deleted = service.delete(jobId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-servicedetails-lambda"));
    }
}