package com.ordersphere.order.service;

import com.ordersphere.common.exception.ResourceNotFoundException;
import com.ordersphere.inventory.domain.Inventory;
import com.ordersphere.inventory.repository.InventoryRepository;
import com.ordersphere.inventory.service.InventoryService;
import com.ordersphere.order.domain.Order;
import com.ordersphere.order.domain.OrderItem;
import com.ordersphere.order.dto.CreateOrderResponse;
import com.ordersphere.order.dto.OrderItemRequest;
import com.ordersphere.order.dto.OrderItemResponse;
import com.ordersphere.order.dto.OrderStatus;
import com.ordersphere.order.repository.OrderRepository;
import com.ordersphere.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, InventoryService inventoryService, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public CreateOrderResponse placeOrder(List<OrderItemRequest> items) {
        validateOrderItems(items);
        Order order = new Order(OrderStatus.PENDING);
        reserveInventoryAndAddItem(order, items);
        order.markAsConfirmed();
        Order savedOrder = orderRepository.save(order);
        return mapToCreateOrderResponse(savedOrder);
    }

    private void reserveInventoryAndAddItem(Order order, List<OrderItemRequest> items) {
        items.forEach(item -> {
            inventoryService.reserveStock(item.getProductId(), item.getQuantity());
            OrderItem orderItem = new OrderItem(item.getProductId(), item.getQuantity());
            order.addOrderItem(orderItem);
        });
    }

    private void validateOrderItems(List<OrderItemRequest> items) {
        if(items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        items.forEach(item -> {
            if(item.getProductId() == null) {
                throw new IllegalArgumentException("Product ID is required for each order item");
            }
            if(item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero for product ID: " + item.getProductId());
            }
        });

        items.forEach(item ->
                        productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId())));
    }

    private CreateOrderResponse mapToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .items(order.getOrderItems().stream()
                        .map(item -> new OrderItemResponse(item.getProductId(), item.getQuantity()))
                        .toList())
                .build();
    }

    public CreateOrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return mapToCreateOrderResponse(order);
    }
}
