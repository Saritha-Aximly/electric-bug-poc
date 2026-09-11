package com.aximly.electricbug.vehicledetails.controller;

import com.aximly.electricbug.vehicledetails.dto.JobVehicleDetailsDto;
import com.aximly.electricbug.vehicledetails.service.JobVehicleDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/vehicle-details")
@Tag(name = "Job Vehicle Details", description = "Vehicle details for a job (Section 2 of the job sheet)")
public class JobVehicleDetailsController {

    private final JobVehicleDetailsService service;

    public JobVehicleDetailsController(JobVehicleDetailsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get vehicle details for a job")
    public ResponseEntity<?> get(@PathVariable Integer jobId) {
        return service.getByJobId(jobId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create vehicle details for a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobVehicleDetailsDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping
    @Operation(summary = "Update vehicle details for a job")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @RequestBody JobVehicleDetailsDto dto) {
        boolean updated = service.update(jobId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete vehicle details for a job")
    public ResponseEntity<?> delete(@PathVariable Integer jobId) {
        boolean deleted = service.delete(jobId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-vehicledetails-lambda"));
    }
}