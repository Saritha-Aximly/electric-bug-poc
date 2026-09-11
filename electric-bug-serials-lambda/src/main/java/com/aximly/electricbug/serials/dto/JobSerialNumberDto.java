package com.aximly.electricbug.serials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSerialNumberDto {
    private Integer id;
    private Integer jobId;
    private Integer jobProductId;
    private String serialNumber;
}