package com.ordersphere.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Total quantity is required")
    @Min(value = 0, message = "Total quantity must be non-negative")
    private Long totalQuantity;
}
