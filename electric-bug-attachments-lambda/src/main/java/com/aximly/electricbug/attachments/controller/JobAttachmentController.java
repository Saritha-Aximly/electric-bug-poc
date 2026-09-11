package com.aximly.electricbug.attachments.controller;

import com.aximly.electricbug.attachments.dto.JobAttachmentDto;
import com.aximly.electricbug.attachments.service.JobAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/attachments")
@Tag(name = "Job Attachments", description = "File attachments across all upload points in the job sheet")
public class JobAttachmentController {

    private final JobAttachmentService service;

    public JobAttachmentController(JobAttachmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all attachments for a job")
    public ResponseEntity<?> getAll(@PathVariable Integer jobId) {
        return ResponseEntity.ok(service.getByJobId(jobId));
    }

    @GetMapping("/section/{section}")
    @Operation(summary = "Get attachments for a job filtered by section")
    public ResponseEntity<?> getBySection(@PathVariable Integer jobId, @PathVariable String section) {
        return ResponseEntity.ok(service.getByJobIdAndSection(jobId, section));
    }

    @GetMapping("/{attachmentId}")
    @Operation(summary = "Get a single attachment")
    public ResponseEntity<?> getOne(@PathVariable Integer jobId, @PathVariable Integer attachmentId) {
        return service.getById(attachmentId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Add an attachment to a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobAttachmentDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{attachmentId}")
    @Operation(summary = "Update an attachment")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @PathVariable Integer attachmentId, @RequestBody JobAttachmentDto dto) {
        boolean updated = service.update(attachmentId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete an attachment")
    public ResponseEntity<?> delete(@PathVariable Integer jobId, @PathVariable Integer attachmentId) {
        boolean deleted = service.delete(attachmentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-attachments-lambda"));
    }
}