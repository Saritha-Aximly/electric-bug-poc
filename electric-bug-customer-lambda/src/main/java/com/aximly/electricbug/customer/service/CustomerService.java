package com.aximly.electricbug.customer.service;

import com.aximly.electricbug.customer.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(int id);
    CustomerDto createCustomer(CustomerDto customer);
    boolean updateCustomer(int id, CustomerDto customer);
    boolean deleteCustomer(int id);
}