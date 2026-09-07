package com.aximly.electricbug.order.service;

import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> getAllOrders();
    Optional<OrderDto> getOrderById(Integer orderId);
    OrderDto createOrder(OrderDto order);
    boolean updateOrder(Integer orderId, OrderDto order);
    boolean deleteOrder(Integer orderId);

    List<LaybyOrderDto> getLaybyOrders();
    Optional<LaybyOrderDto> getLaybyOrderById(Integer laybyId);
    LaybyOrderDto createLaybyOrder(LaybyOrderDto layby);
    boolean updateLaybyOrder(Integer laybyId, LaybyOrderDto layby);
    boolean deleteLaybyOrder(Integer laybyId);
}