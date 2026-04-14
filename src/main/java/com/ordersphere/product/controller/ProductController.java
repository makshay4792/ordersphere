package com.ordersphere.product.controller;

import com.ordersphere.product.dto.CreateProductRequest;
import com.ordersphere.product.dto.ProductResponse;
import com.ordersphere.product.dto.UpdateProductRequest;
import com.ordersphere.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product APIs", description = "APIs for managing products")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request){
        return productService.createProduct(request);
    }

    @Operation(summary = "Update an existing product", description = "Update the details of an existing product by its ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        return productService.updateProduct(id, request);
    }

    @Operation(summary = "Deactivate a product", description = "Deactivate a product by its ID")
    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@PathVariable Long id){
        productService.deactivateProduct(id);
    }
}
