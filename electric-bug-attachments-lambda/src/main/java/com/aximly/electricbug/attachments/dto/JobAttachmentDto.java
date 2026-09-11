package com.aximly.electricbug.attachments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobAttachmentDto {
    private Integer id;
    private Integer jobId;
    private String section; // 'vehicle_arrival' | 'instructions' | 'delivery' | 'installer_other' | 'product_images'
    private String fileUrl;
    private Long fileSizeBytes;
    private String notes;
    private OffsetDateTime uploadedAt; // read-only
}