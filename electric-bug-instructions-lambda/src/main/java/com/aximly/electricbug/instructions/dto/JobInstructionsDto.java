package com.aximly.electricbug.instructions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstructionsDto {
    private Integer jobId;
    private String instructionsText;
}