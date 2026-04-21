package com.ordersphere.inventory.service;

import com.ordersphere.common.exception.ResourceNotFoundException;
import com.ordersphere.inventory.domain.Inventory;
import com.ordersphere.inventory.dto.CreateInventoryRequest;
import com.ordersphere.inventory.dto.InventoryResponse;
import com.ordersphere.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest request) {
        inventoryRepository.findByProductId(request.getProductId()).ifPresent(inv -> {
            throw new IllegalStateException("Inventory already exists for product ID: " + request.getProductId());
        });
        Inventory inventory = new Inventory(request.getProductId(), request.getTotalQuantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToResponse(savedInventory);
    }

    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));
        return mapToResponse(inventory);
    }

    public InventoryResponse addStock(Long productId, Long quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));
        inventory.addStock(quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToResponse(updatedInventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .totalQuantity(inventory.getTotalQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(inventory.getAvailableQuantity())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
