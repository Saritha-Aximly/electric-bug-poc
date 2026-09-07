package com.aximly.electricbug.customer.service.impl;

import com.aximly.electricbug.customer.dao.CustomerDao;
import com.aximly.electricbug.customer.dto.CustomerDto;
import com.aximly.electricbug.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    public CustomerDto getCustomerById(int id) {
        return customerDao.getCustomerById(id);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customer) {
        return customerDao.createCustomer(customer);
    }

    @Override
    public boolean updateCustomer(int id, CustomerDto customer) {
        customer.setCustomerId(id);
        return customerDao.updateCustomer(customer);
    }

    @Override
    public boolean deleteCustomer(int id) {
        return customerDao.deleteCustomer(id);
    }
}