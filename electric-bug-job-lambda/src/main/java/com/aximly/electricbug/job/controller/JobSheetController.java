package com.aximly.electricbug.job.controller;

import com.aximly.electricbug.job.dto.JobSheetDto;
import com.aximly.electricbug.job.service.JobSheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Sheets", description = "Job sheet records — one per installation job")
public class JobSheetController {

    private final JobSheetService jobSheetService;

    public JobSheetController(JobSheetService jobSheetService) {
        this.jobSheetService = jobSheetService;
    }

    @GetMapping
    @Operation(summary = "Get all job sheets")
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobSheetService.getAllJobs());
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Get a job sheet by ID")
    public ResponseEntity<?> getJobById(@PathVariable Integer jobId) {
        return jobSheetService.getJobById(jobId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get job sheets filtered by status")
    public ResponseEntity<?> getJobsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(jobSheetService.getJobsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Create a new job sheet")
    public ResponseEntity<?> createJob(@RequestBody JobSheetDto job) {
        return ResponseEntity.ok(jobSheetService.createJob(job));
    }

    @PutMapping("/{jobId}")
    @Operation(summary = "Update an existing job sheet")
    public ResponseEntity<?> updateJob(@PathVariable Integer jobId, @RequestBody JobSheetDto job) {
        boolean updated = jobSheetService.updateJob(jobId, job);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{jobId}")
    @Operation(summary = "Delete a job sheet")
    public ResponseEntity<?> deleteJob(@PathVariable Integer jobId) {
        boolean deleted = jobSheetService.deleteJob(jobId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-job-lambda"));
    }
}