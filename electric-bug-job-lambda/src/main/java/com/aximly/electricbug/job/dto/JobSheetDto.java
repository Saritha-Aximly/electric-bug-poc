package com.aximly.electricbug.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSheetDto {
    private Integer jobId;
    private String jobCode;
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String orderType;          // 'sales_order' | 'layby' | 'other'
    private Integer salesOrderId;
    private String salesOrderDisplay;
    private Integer laybyId;
    private String laybyDisplay;
    private String address;
    private String rwdRep;
    private Boolean rwdNotify;
    private Boolean tape;
    private String installerComments;
    private Boolean fittingShelf;
    private String additionalProducts;
    private Double productTotal;
    private Double installationCost;
    private Double estimatedTotal;     // read-only, DB-generated
    private String depositNumber;
    private String paidInvoiceNumber;
    private String status;             // 'pending' | 'in_progress' | 'completed' | 'cancelled'
    private Boolean missingStock;
    private LocalDate jobCompletedDate;
    private OffsetDateTime createdAt;  // read-only
    private OffsetDateTime updatedAt;  // read-only
}