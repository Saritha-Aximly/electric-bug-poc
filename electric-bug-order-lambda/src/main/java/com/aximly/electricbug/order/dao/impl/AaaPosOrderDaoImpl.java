package com.aximly.electricbug.order.dao.impl;

import com.aximly.electricbug.order.dao.OrderDao;
import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "externalApiFlag", havingValue = "true")
public class AaaPosOrderDaoImpl implements OrderDao {

    private final WebClient aaaPosWebClient;

    public AaaPosOrderDaoImpl(WebClient aaaPosWebClient) {
        this.aaaPosWebClient = aaaPosWebClient;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        // TODO: replace "/orders" with the real AAAPOS endpoint once confirmed,
        // and map its actual response shape into OrderDto below.
        return aaaPosWebClient.get()
                .uri("/orders")
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .collectList()
                .block();
    }

    @Override
    public List<LaybyOrderDto> getLaybyOrders() {
        // TODO: replace "/orders/layby" with the real AAAPOS endpoint once confirmed
        return aaaPosWebClient.get()
                .uri("/orders/layby")
                .retrieve()
                .bodyToFlux(LaybyOrderDto.class)
                .collectList()
                .block();
    }

    @Override
    public Optional<OrderDto> getOrderById(Integer orderId) {
        throw new UnsupportedOperationException(
                "Fetching a single order by ID via AAA POS API is not yet implemented.");
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        throw new UnsupportedOperationException(
                "Creating orders via AAA POS API is not supported — orders are managed in AAA POS directly.");
    }

    @Override
    public boolean updateOrder(OrderDto order) {
        throw new UnsupportedOperationException(
                "Updating orders via AAA POS API is not supported — orders are managed in AAA POS directly.");
    }

    @Override
    public boolean deleteOrder(Integer orderId) {
        throw new UnsupportedOperationException(
                "Deleting orders via AAA POS API is not supported — orders are managed in AAA POS directly.");
    }

    @Override
    public Optional<LaybyOrderDto> getLaybyOrderById(Integer laybyId) {
        throw new UnsupportedOperationException(
                "Fetching a single LayBy order by ID via AAA POS API is not yet implemented.");
    }

    @Override
    public LaybyOrderDto createLaybyOrder(LaybyOrderDto layby) {
        throw new UnsupportedOperationException(
                "Creating LayBy orders via AAA POS API is not supported — LayBy orders are managed in AAA POS directly.");
    }

    @Override
    public boolean updateLaybyOrder(LaybyOrderDto layby) {
        throw new UnsupportedOperationException(
                "Updating LayBy orders via AAA POS API is not supported — LayBy orders are managed in AAA POS directly.");
    }

    @Override
    public boolean deleteLaybyOrder(Integer laybyId) {
        throw new UnsupportedOperationException(
                "Deleting LayBy orders via AAA POS API is not supported — LayBy orders are managed in AAA POS directly.");
    }
}