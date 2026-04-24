package com.ordersphere.order.controller;

import com.ordersphere.order.dto.CreateOrderRequest;
import com.ordersphere.order.dto.CreateOrderResponse;
import com.ordersphere.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order APIs", description = "APIs for managing order")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(request.getItems());
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public CreateOrderResponse getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public CreateOrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
