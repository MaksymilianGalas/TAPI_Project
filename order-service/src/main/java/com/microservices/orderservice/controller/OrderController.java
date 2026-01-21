package com.microservices.orderservice.controller;

import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /api/orders - Fetching all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        log.info("GET /api/orders/{} - Fetching order by id", id);
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        log.info("GET /api/orders/number/{} - Fetching order by order number", orderNumber);
        return orderService.getOrderByOrderNumber(orderNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable String customerId) {
        log.info("GET /api/orders/customer/{} - Fetching orders for customer", customerId);
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        log.info("GET /api/orders/status/{} - Fetching orders with status", status);
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        log.info("POST /api/orders - Creating new order");
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            log.error("Error creating order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Order> updateOrder(@PathVariable String id, @Valid @RequestBody Order order) {
        log.info("PUT /api/orders/{} - Updating order", id);
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            log.error("Error updating order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        log.info("DELETE /api/orders/{} - Deleting order", id);
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting order: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
