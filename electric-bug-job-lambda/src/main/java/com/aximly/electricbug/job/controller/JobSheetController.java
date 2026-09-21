package com.aximly.electricbug.job.controller;

import com.aximly.electricbug.job.dto.JobSheetDto;
import com.aximly.electricbug.job.service.JobSheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartSyncExecutionRequest;
import software.amazon.awssdk.services.sfn.model.StartSyncExecutionResponse;
import software.amazon.awssdk.services.sfn.model.SyncExecutionStatus;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Sheets", description = "Job sheet records — one per installation job")
public class JobSheetController {

    private final JobSheetService jobSheetService;
    private final SfnClient sfnClient;

    @Value("${jobsheet.workflow.state-machine-arn}")
    private String stateMachineArn;

    public JobSheetController(JobSheetService jobSheetService, SfnClient sfnClient) {
        this.jobSheetService = jobSheetService;
        this.sfnClient = sfnClient;
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

    @PostMapping("/full")
    @Operation(summary = "Create a full job sheet via the orchestrated workflow")
    public ResponseEntity<?> createFullJobSheet(@RequestBody String workflowInput) {
        StartSyncExecutionResponse response = sfnClient.startSyncExecution(
                StartSyncExecutionRequest.builder()
                        .stateMachineArn(stateMachineArn)
                        .input(workflowInput)
                        .build());

        if (response.status() == SyncExecutionStatus.SUCCEEDED) {
            return ResponseEntity.ok(response.output());
        } else {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", response.error() != null ? response.error() : "Unknown",
                    "cause", response.cause() != null ? response.cause() : "No details available"
            ));
        }
    }
}