package com.aximly.electricbug.vehicledetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobVehicleDetailsDto {
    private Integer jobId;
    private String make;
    private String model;
    private Integer year;
    private String registration;
    private String arrivalPhotoUrl;
}