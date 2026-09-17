package com.aximly.electricbug.staff.service.impl;

import com.aximly.electricbug.staff.dao.StaffDao;
import com.aximly.electricbug.staff.dto.StaffDto;
import com.aximly.electricbug.staff.service.StaffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffDao staffDao;

    public StaffServiceImpl(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    @Override
    public List<StaffDto> getAllStaff(boolean activeOnly) {
        return staffDao.getAllStaff(activeOnly);
    }

    @Override
    public List<StaffDto> getStaffByRole(String role, boolean activeOnly) {
        return staffDao.getStaffByRole(role, activeOnly);
    }

    @Override
    public Optional<StaffDto> getStaffById(Integer staffId) {
        return staffDao.getStaffById(staffId);
    }

    @Override
    public StaffDto createStaff(StaffDto staff) {
        return staffDao.createStaff(staff);
    }

    @Override
    public boolean updateStaff(Integer staffId, StaffDto staff) {
        staff.setStaffId(staffId);
        return staffDao.updateStaff(staff);
    }

    @Override
    public boolean deactivateStaff(Integer staffId) {
        return staffDao.deactivateStaff(staffId);
    }
}