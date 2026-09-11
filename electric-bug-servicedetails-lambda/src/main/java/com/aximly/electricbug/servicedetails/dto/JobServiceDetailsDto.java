package com.aximly.electricbug.servicedetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobServiceDetailsDto {
    private Integer jobId;
    private LocalDate serviceDate;
    private String jobInTime;
    private String pickupTime;
    private Double estimatedHours;
    private Integer bookingRepId;
    private Integer plannerId;
    private String location;
}