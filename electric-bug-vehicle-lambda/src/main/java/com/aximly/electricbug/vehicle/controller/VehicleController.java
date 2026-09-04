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

    @GetMapping("/makes")
    @Operation(summary = "Get all vehicle makes")
    public ResponseEntity<List<VehicleMakeDto>> getMakes() {
        return ResponseEntity.ok(vehicleService.getAllMakes());
    }

    @GetMapping("/models")
    @Operation(summary = "Get all models for a given make")
    public ResponseEntity<List<VehicleModelDto>> getModels(@RequestParam String make) {
        return ResponseEntity.ok(vehicleService.getModelsForMake(make));
    }
}