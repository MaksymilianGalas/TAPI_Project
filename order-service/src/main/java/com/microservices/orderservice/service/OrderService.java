package com.microservices.orderservice.service;

import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        log.info("Fetching order by id: {}", id);
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> getOrdersByCustomerId(String customerId) {
        log.info("Fetching orders for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersByStatus(String status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Order order) {
        log.info("Creating new order for customer: {}", order.getCustomerId());

        // Generate order number if not provided
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (orderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new RuntimeException("Order number already exists: " + order.getOrderNumber());
        }

        // Calculate total amount
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            BigDecimal total = order.getItems().stream()
                    .map(item -> {
                        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        item.setSubtotal(subtotal);
                        return subtotal;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);
        }

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("PENDING");
        }

        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Order orderDetails) {
        log.info("Updating order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Check if order number is being changed and if it already exists
        if (!order.getOrderNumber().equals(orderDetails.getOrderNumber()) &&
                orderRepository.existsByOrderNumber(orderDetails.getOrderNumber())) {
            throw new RuntimeException("Order number already exists: " + orderDetails.getOrderNumber());
        }

        order.setOrderNumber(orderDetails.getOrderNumber());
        order.setCustomerId(orderDetails.getCustomerId());
        order.setCustomerName(orderDetails.getCustomerName());
        order.setCustomerEmail(orderDetails.getCustomerEmail());
        order.setItems(orderDetails.getItems());
        order.setStatus(orderDetails.getStatus());
        order.setShippingAddress(orderDetails.getShippingAddress());
        order.setNotes(orderDetails.getNotes());

        // Recalculate total amount
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            BigDecimal total = order.getItems().stream()
                    .map(item -> {
                        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        item.setSubtotal(subtotal);
                        return subtotal;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);
        }

        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        log.info("Deleting order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
    }
}
