package com.aximly.electricbug.customer.dao.impl;

import com.aximly.electricbug.customer.dao.CustomerDao;
import com.aximly.electricbug.customer.dto.CustomerDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "externalApiFlag", havingValue = "true")
public class AaaPosCustomerDaoImpl implements CustomerDao {

    private final WebClient aaaPosWebClient;

    public AaaPosCustomerDaoImpl(WebClient aaaPosWebClient) {
        this.aaaPosWebClient = aaaPosWebClient;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        // TODO: real AAAPOS endpoint + response mapping once confirmed
        return aaaPosWebClient.get().uri("/customers").retrieve()
                .bodyToFlux(CustomerDto.class).collectList().block();
    }

    @Override
    public CustomerDto getCustomerById(int id) {
        // TODO: real AAAPOS endpoint once confirmed
        return aaaPosWebClient.get().uri("/customers/" + id).retrieve()
                .bodyToMono(CustomerDto.class).block();
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customer) {
        throw new UnsupportedOperationException(
                "Creating customers via AAA POS API is not supported — customer records are managed in AAA POS directly.");
    }

    @Override
    public boolean updateCustomer(CustomerDto customer) {
        throw new UnsupportedOperationException(
                "Updating customers via AAA POS API is not supported — customer records are managed in AAA POS directly.");
    }

    @Override
    public boolean deleteCustomer(int id) {
        throw new UnsupportedOperationException(
                "Deleting customers via AAA POS API is not supported — customer records are managed in AAA POS directly.");
    }
}