package com.aximly.electricbug.staff.dao;

import com.aximly.electricbug.staff.dto.StaffDto;

import java.util.List;
import java.util.Optional;

public interface StaffDao {
    List<StaffDto> getAllStaff(boolean activeOnly);
    List<StaffDto> getStaffByRole(String role, boolean activeOnly);
    Optional<StaffDto> getStaffById(Integer staffId);
    StaffDto createStaff(StaffDto staff);
    boolean updateStaff(StaffDto staff);
    boolean deactivateStaff(Integer staffId);
}