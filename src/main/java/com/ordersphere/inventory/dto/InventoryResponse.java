package com.ordersphere.inventory.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Long totalQuantity;
    private Long reservedQuantity;
    private Long availableQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
