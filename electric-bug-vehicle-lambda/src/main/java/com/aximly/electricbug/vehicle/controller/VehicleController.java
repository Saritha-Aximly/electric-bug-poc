package com.aximly.electricbug.vehicle.controller;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
import com.aximly.electricbug.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicle makes and models reference data")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("service", "electric-bug-vehicle-lambda", "status", "UP"));
    }

    // ---- Makes ----

    @GetMapping("/makes")
    @Operation(summary = "Get all vehicle makes")
    public ResponseEntity<List<VehicleMakeDto>> getMakes() {
        return ResponseEntity.ok(vehicleService.getAllMakes());
    }

    @PostMapping("/makes")
    @Operation(summary = "Create a new vehicle make")
    public ResponseEntity<?> createMake(@RequestBody VehicleMakeDto make) {
        return ResponseEntity.ok(vehicleService.createMake(make));
    }

    @PutMapping("/makes/{makeId}")
    @Operation(summary = "Update an existing vehicle make")
    public ResponseEntity<?> updateMake(@PathVariable Integer makeId, @RequestBody VehicleMakeDto make) {
        boolean updated = vehicleService.updateMake(makeId, make);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/makes/{makeId}")
    @Operation(summary = "Delete a vehicle make")
    public ResponseEntity<?> deleteMake(@PathVariable Integer makeId) {
        boolean deleted = vehicleService.deleteMake(makeId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---- Models ----

    @GetMapping("/models")
    @Operation(summary = "Get all models for a given make")
    public ResponseEntity<List<VehicleModelDto>> getModels(@RequestParam String make) {
        return ResponseEntity.ok(vehicleService.getModelsForMake(make));
    }

    @PostMapping("/models")
    @Operation(summary = "Create a new vehicle model")
    public ResponseEntity<?> createModel(@RequestBody VehicleModelDto model) {
        return ResponseEntity.ok(vehicleService.createModel(model));
    }

    @PutMapping("/models/{modelId}")
    @Operation(summary = "Update an existing vehicle model")
    public ResponseEntity<?> updateModel(@PathVariable Integer modelId, @RequestBody VehicleModelDto model) {
        boolean updated = vehicleService.updateModel(modelId, model);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/models/{modelId}")
    @Operation(summary = "Delete a vehicle model")
    public ResponseEntity<?> deleteModel(@PathVariable Integer modelId) {
        boolean deleted = vehicleService.deleteModel(modelId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}