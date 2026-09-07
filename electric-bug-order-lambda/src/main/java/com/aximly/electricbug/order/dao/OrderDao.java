package com.aximly.electricbug.order.dao;

import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    List<OrderDto> getAllOrders();
    Optional<OrderDto> getOrderById(Integer orderId);
    OrderDto createOrder(OrderDto order);
    boolean updateOrder(OrderDto order);
    boolean deleteOrder(Integer orderId);

    List<LaybyOrderDto> getLaybyOrders();
    Optional<LaybyOrderDto> getLaybyOrderById(Integer laybyId);
    LaybyOrderDto createLaybyOrder(LaybyOrderDto layby);
    boolean updateLaybyOrder(LaybyOrderDto layby);
    boolean deleteLaybyOrder(Integer laybyId);
}