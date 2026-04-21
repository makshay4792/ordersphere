package com.ordersphere.inventory.domain;

import com.ordersphere.common.entity.BaseEntity;
import com.ordersphere.common.exception.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = "product_id"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Min(0)
    @Column(name = "total_quantity", nullable = false)
    private Long totalQuantity;

    @Min(0)
    @Column(name = "reserved_quantity", nullable = false)
    private Long reservedQuantity = 0L;

    @Min(0)
    @Column(name = "available_quantity", nullable = false)
    private Long availableQuantity = 0L;

    public Inventory(Long productId, Long totalQuantity) {
        validateNonNegative(totalQuantity);
        this.productId = productId;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = 0L;
        this.availableQuantity = totalQuantity;
    }

    public void addStock(Long quantity) {
        validateNonNegative(quantity);
        this.totalQuantity += quantity;
        this.availableQuantity += quantity;
    }

    public void reserve(Long quantity) {
        validateNonNegative(quantity);
        if (quantity > this.availableQuantity) {
            throw new InsufficientStockException("Not enough available stock to reserve");
        }
        this.reservedQuantity += quantity;
        this.availableQuantity -= quantity;
    }

    public void release(Long quantity) {
        validateNonNegative(quantity);
        if (quantity > this.reservedQuantity) {
            throw new IllegalStateException("Cannot release more than reserved quantity");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
    }

    private void validateNonNegative(Long quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }
}
