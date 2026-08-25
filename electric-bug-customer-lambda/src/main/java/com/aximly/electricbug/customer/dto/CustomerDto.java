package com.aximly.electricbug.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer customerId;
    private String givenNames;
    private String surname;
    private String email;
    private String phone;
}