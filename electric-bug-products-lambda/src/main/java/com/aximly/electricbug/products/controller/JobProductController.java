package com.aximly.electricbug.products.controller;

import com.aximly.electricbug.products.dto.JobProductDto;
import com.aximly.electricbug.products.service.JobProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/products")
@Tag(name = "Job Products", description = "Products/equipment attached to a job (Section 7)")
public class JobProductController {

    private final JobProductService service;

    public JobProductController(JobProductService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all products for a job")
    public ResponseEntity<?> getAll(@PathVariable Integer jobId) {
        return ResponseEntity.ok(service.getByJobId(jobId));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a single product line")
    public ResponseEntity<?> getOne(@PathVariable Integer jobId, @PathVariable Integer productId) {
        return service.getById(productId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Add a product line to a job")
    public ResponseEntity<?> create(@PathVariable Integer jobId, @RequestBody JobProductDto dto) {
        dto.setJobId(jobId);
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product line")
    public ResponseEntity<?> update(@PathVariable Integer jobId, @PathVariable Integer productId, @RequestBody JobProductDto dto) {
        boolean updated = service.update(productId, dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove a product line")
    public ResponseEntity<?> delete(@PathVariable Integer jobId, @PathVariable Integer productId) {
        boolean deleted = service.delete(productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-products-lambda"));
    }
}