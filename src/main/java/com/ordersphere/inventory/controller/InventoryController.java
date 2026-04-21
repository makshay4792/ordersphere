package com.ordersphere.inventory.controller;

import com.ordersphere.inventory.dto.AddStockRequest;
import com.ordersphere.inventory.dto.CreateInventoryRequest;
import com.ordersphere.inventory.dto.InventoryResponse;
import com.ordersphere.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory APIs", description = "APIs for managing inventory")
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(@Valid @RequestBody CreateInventoryRequest request) {
        return inventoryService.createInventory(request);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping("/{productId}/stock")
    public InventoryResponse addStock(@PathVariable Long productId, @Valid @RequestBody AddStockRequest request) {
        return inventoryService.addStock(productId, request.getQuantity());
    }
}
