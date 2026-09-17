package com.aximly.electricbug.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Integer staffId;
    private String fullName;
    private String role;      // 'BOOKING_REP' | 'PLANNER' | 'INSTALLER' (whatever your convention is)
    private Boolean active;
}