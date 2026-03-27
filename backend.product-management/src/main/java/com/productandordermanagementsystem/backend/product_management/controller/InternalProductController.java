package com.productandordermanagementsystem.backend.product_management.controller;

import com.productandordermanagementsystem.backend.product_management.dto.product.InventoryDeductionRequest;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductResponse;
import com.productandordermanagementsystem.backend.product_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

    private final ProductService productService;

    public InternalProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/deduct")
    public ResponseEntity<ProductResponse> deductInventory(@Valid @RequestBody InventoryDeductionRequest request) {
        return ResponseEntity.ok(productService.deductInventory(request));
    }
}
