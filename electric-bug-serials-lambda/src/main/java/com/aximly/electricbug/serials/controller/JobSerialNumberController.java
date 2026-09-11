package com.aximly.electricbug.serials.controller;

import com.aximly.electricbug.serials.dto.JobSerialNumberDto;
import com.aximly.electricbug.serials.service.JobSerialNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/serials")
@Tag(name = "Job Serial Numbers", description = "Serial numbers recorded for a job's products (Section 8)")
public class JobSerialNumberController {

    private final JobSerialNumberService service;

    public JobSerialNumberController(JobSerialNumberService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all serial numbers for a job")
    public ResponseEntity<?> getAll(@PathVariable Integer jobId) {
        return ResponseEntity.ok(service.getByJobId(jobId));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get serial numbers for a specific product line")
    public ResponseEntity<?> getByProduct(@PathVariable Integer jobId, @PathVariable Integer productId) {
        return ResponseEntity.ok(service.getByProductId(productId));
    }

    @GetMapping("/{serialId}")
    @Operation(summary = "Get a single serial number entry")
    public ResponseEntity<?> getOne(@PathVariable Integer jobId, @PathVariable Integer serialId) {
        return service.getById(serialId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Record a serial number for a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobSerialNumberDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{serialId}")
    @Operation(summary = "Update a serial number entry")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @PathVariable Integer serialId, @RequestBody JobSerialNumberDto dto) {
        boolean updated = service.update(serialId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{serialId}")
    @Operation(summary = "Delete a serial number entry")
    public ResponseEntity<?> delete(@PathVariable Integer jobId, @PathVariable Integer serialId) {
        boolean deleted = service.delete(serialId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-serials-lambda"));
    }
}