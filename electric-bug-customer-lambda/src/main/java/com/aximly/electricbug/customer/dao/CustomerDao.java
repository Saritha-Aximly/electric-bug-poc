package com.aximly.electricbug.customer.dao;

import com.aximly.electricbug.customer.dto.CustomerDto;
import java.util.List;

public interface CustomerDao {
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(int id);
}