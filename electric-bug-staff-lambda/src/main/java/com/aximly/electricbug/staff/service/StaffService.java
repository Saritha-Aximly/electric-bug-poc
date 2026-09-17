package com.aximly.electricbug.staff.service;

import com.aximly.electricbug.staff.dto.StaffDto;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<StaffDto> getAllStaff(boolean activeOnly);
    List<StaffDto> getStaffByRole(String role, boolean activeOnly);
    Optional<StaffDto> getStaffById(Integer staffId);
    StaffDto createStaff(StaffDto staff);
    boolean updateStaff(Integer staffId, StaffDto staff);
    boolean deactivateStaff(Integer staffId);
}