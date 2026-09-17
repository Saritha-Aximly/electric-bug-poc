package com.aximly.electricbug.staff.controller;

import com.aximly.electricbug.staff.dto.StaffDto;
import com.aximly.electricbug.staff.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "Staff records — booking reps, planners, installers")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    @Operation(summary = "Get all staff, optionally filtered by role, active only by default")
    public ResponseEntity<?> getAllStaff(
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        if (role != null) {
            return ResponseEntity.ok(staffService.getStaffByRole(role, activeOnly));
        }
        return ResponseEntity.ok(staffService.getAllStaff(activeOnly));
    }

    @GetMapping("/{staffId}")
    @Operation(summary = "Get a staff member by ID")
    public ResponseEntity<?> getStaffById(@PathVariable Integer staffId) {
        return staffService.getStaffById(staffId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new staff member")
    public ResponseEntity<?> createStaff(@RequestBody StaffDto staff) {
        return ResponseEntity.ok(staffService.createStaff(staff));
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Update an existing staff member")
    public ResponseEntity<?> updateStaff(@PathVariable Integer staffId, @RequestBody StaffDto staff) {
        boolean updated = staffService.updateStaff(staffId, staff);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Deactivate a staff member (soft delete — preserves FK history on old jobs)")
    public ResponseEntity<?> deactivateStaff(@PathVariable Integer staffId) {
        boolean deactivated = staffService.deactivateStaff(staffId);
        return deactivated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-staff-lambda"));
    }
}