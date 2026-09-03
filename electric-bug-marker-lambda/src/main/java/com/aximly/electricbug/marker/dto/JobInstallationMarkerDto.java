package com.aximly.electricbug.marker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstallationMarkerDto {
    private Integer id;
    private Integer jobId;
    private String markerType;
    private String label;
    private String notes;
    private Double posX;
    private Double posY;
}