package com.aximly.electricbug.vehicle.controller;

import com.aximly.electricbug.vehicle.dto.VehicleMakeDto;
import com.aximly.electricbug.vehicle.dto.VehicleModelDto;
import com.aximly.electricbug.vehicle.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("service", "electric-bug-vehicle-lambda", "status", "UP"));
    }

    @GetMapping("/makes")
    public ResponseEntity<List<VehicleMakeDto>> getMakes() {
        return ResponseEntity.ok(vehicleService.getAllMakes());
    }

    @GetMapping("/models")
    public ResponseEntity<List<VehicleModelDto>> getModels(@RequestParam String make) {
        return ResponseEntity.ok(vehicleService.getModelsForMake(make));
    }
}