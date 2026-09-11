package com.aximly.electricbug.instructions.controller;

import com.aximly.electricbug.instructions.dto.JobInstructionsDto;
import com.aximly.electricbug.instructions.service.JobInstructionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/instructions")
@Tag(name = "Job Instructions", description = "Instructions text for a job (Section 4)")
public class JobInstructionsController {

    private final JobInstructionsService service;

    public JobInstructionsController(JobInstructionsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get instructions for a job")
    public ResponseEntity<?> get(@PathVariable Integer jobId) {
        return service.getByJobId(jobId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create instructions for a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobInstructionsDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping
    @Operation(summary = "Update instructions for a job")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @RequestBody JobInstructionsDto dto) {
        boolean updated = service.update(jobId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete instructions for a job")
    public ResponseEntity<?> delete(@PathVariable Integer jobId) {
        boolean deleted = service.delete(jobId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-instructions-lambda"));
    }
}