package com.productandordermanagementsystem.backend.product_management.controller;

import com.productandordermanagementsystem.backend.product_management.dto.product.ProductCreateRequest;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductResponse;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductStatusUpdateRequest;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductUpdateRequest;
import com.productandordermanagementsystem.backend.product_management.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() { return ResponseEntity.ok(productService.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(productService.getById(id)); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody ProductStatusUpdateRequest request) {
        return ResponseEntity.ok(productService.updateStatus(id, request.enabled()));
    }
}
