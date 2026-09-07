package com.aximly.electricbug.order.controller;

import com.aximly.electricbug.order.dto.LaybyOrderDto;
import com.aximly.electricbug.order.dto.OrderDto;
import com.aximly.electricbug.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Orders and LayBy orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ---- Orders ----

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get an order by ID")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an existing order")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId, @RequestBody OrderDto order) {
        boolean updated = orderService.updateOrder(orderId, order);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---- Layby orders ----

    @GetMapping("/layby")
    @Operation(summary = "Get all LayBy orders")
    public ResponseEntity<?> getLaybyOrders() {
        return ResponseEntity.ok(orderService.getLaybyOrders());
    }

    @GetMapping("/layby/{laybyId}")
    @Operation(summary = "Get a LayBy order by ID")
    public ResponseEntity<?> getLaybyOrderById(@PathVariable Integer laybyId) {
        return orderService.getLaybyOrderById(laybyId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/layby")
    @Operation(summary = "Create a new LayBy order")
    public ResponseEntity<?> createLaybyOrder(@RequestBody LaybyOrderDto layby) {
        return ResponseEntity.ok(orderService.createLaybyOrder(layby));
    }

    @PutMapping("/layby/{laybyId}")
    @Operation(summary = "Update an existing LayBy order")
    public ResponseEntity<?> updateLaybyOrder(@PathVariable Integer laybyId, @RequestBody LaybyOrderDto layby) {
        boolean updated = orderService.updateLaybyOrder(laybyId, layby);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/layby/{laybyId}")
    @Operation(summary = "Delete a LayBy order")
    public ResponseEntity<?> deleteLaybyOrder(@PathVariable Integer laybyId) {
        boolean deleted = orderService.deleteLaybyOrder(laybyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-order-lambda"));
    }
}