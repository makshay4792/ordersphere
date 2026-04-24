package com.ordersphere.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateOrderResponse {
    private Long orderId;
    private OrderStatus status;
    private List<OrderItemResponse> items;
}
