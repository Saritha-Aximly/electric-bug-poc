package com.aximly.electricbug.order.service.impl;

import com.aximly.electricbug.order.dao.OrderDao;
import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;
import com.aximly.electricbug.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public Optional<OrderDto> getOrderById(Integer orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        return orderDao.createOrder(order);
    }

    @Override
    public boolean updateOrder(Integer orderId, OrderDto order) {
        order.setOrderId(orderId);
        return orderDao.updateOrder(order);
    }

    @Override
    public boolean deleteOrder(Integer orderId) {
        return orderDao.deleteOrder(orderId);
    }

    @Override
    public List<LaybyOrderDto> getLaybyOrders() {
        return orderDao.getLaybyOrders();
    }

    @Override
    public Optional<LaybyOrderDto> getLaybyOrderById(Integer laybyId) {
        return orderDao.getLaybyOrderById(laybyId);
    }

    @Override
    public LaybyOrderDto createLaybyOrder(LaybyOrderDto layby) {
        return orderDao.createLaybyOrder(layby);
    }

    @Override
    public boolean updateLaybyOrder(Integer laybyId, LaybyOrderDto layby) {
        layby.setLaybyId(laybyId);
        return orderDao.updateLaybyOrder(layby);
    }

    @Override
    public boolean deleteLaybyOrder(Integer laybyId) {
        return orderDao.deleteLaybyOrder(laybyId);
    }
}