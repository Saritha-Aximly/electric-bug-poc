package com.aximly.electricbug.customer.dao;

import com.aximly.electricbug.customer.dto.CustomerDto;

import java.util.List;

public interface CustomerDao {
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(int id);
    CustomerDto createCustomer(CustomerDto customer);
    boolean updateCustomer(CustomerDto customer);
    boolean deleteCustomer(int id);
}