package com.aximly.electricbug.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobProductDto {
    private Integer id;
    private Integer jobId;
    private Double stockId;
    private String productName;
    private String barcode;
    private Double qty;
    private Double price;
    private Double lineTotal;
}