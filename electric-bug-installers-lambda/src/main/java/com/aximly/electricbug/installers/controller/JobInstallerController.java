package com.aximly.electricbug.installers.controller;

import com.aximly.electricbug.installers.dto.JobInstallerDto;
import com.aximly.electricbug.installers.service.JobInstallerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/installers")
@Tag(name = "Job Installers", description = "Installers assigned to a job (Section 3 continued)")
public class JobInstallerController {

    private final JobInstallerService service;

    public JobInstallerController(JobInstallerService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all installers for a job")
    public ResponseEntity<?> getAll(@PathVariable Integer jobId) {
        return ResponseEntity.ok(service.getByJobId(jobId));
    }

    @GetMapping("/{installerId}")
    @Operation(summary = "Get a single installer entry")
    public ResponseEntity<?> getOne(@PathVariable Integer jobId, @PathVariable Integer installerId) {
        return service.getById(installerId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Add an installer to a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobInstallerDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{installerId}")
    @Operation(summary = "Update an installer entry")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @PathVariable Integer installerId, @RequestBody JobInstallerDto dto) {
        boolean updated = service.update(installerId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{installerId}")
    @Operation(summary = "Remove an installer from a job")
    public ResponseEntity<?> delete(@PathVariable Integer jobId, @PathVariable Integer installerId) {
        boolean deleted = service.delete(installerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-installers-lambda"));
    }
}