package com.aximly.electricbug.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMakeDto {
    private Integer makeId;
    private String makeName;
}