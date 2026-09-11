package com.aximly.electricbug.installers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstallerDto {
    private Integer id;
    private Integer jobId;
    private Integer staffId;
    private Double hours;
}