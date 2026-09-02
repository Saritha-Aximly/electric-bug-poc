package com.aximly.electricbug.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Integer stockId;
    private String barcode;
    private String description;
    private BigDecimal sellPrice;
    private BigDecimal costPrice;
    private Integer quantity;
    private Integer deptId;
}