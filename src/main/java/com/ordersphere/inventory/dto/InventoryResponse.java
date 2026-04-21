package com.ordersphere.inventory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
