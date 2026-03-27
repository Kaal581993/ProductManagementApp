package com.productandordermanagementsystem.backend.product_management.service;

import com.productandordermanagementsystem.backend.product_management.dto.product.InventoryDeductionRequest;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductCreateRequest;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductResponse;
import com.productandordermanagementsystem.backend.product_management.dto.product.ProductUpdateRequest;
import com.productandordermanagementsystem.backend.product_management.entity.Product;
import com.productandordermanagementsystem.backend.product_management.exception.BadRequestException;
import com.productandordermanagementsystem.backend.product_management.exception.ResourceNotFoundException;
import com.productandordermanagementsystem.backend.product_management.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setEnabled(true);
        return toResponse(repository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return repository.findByEnabledTrueOrderByIdAsc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = find(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setEnabled(request.enabled());
        return toResponse(repository.save(product));
    }

    @Transactional
    public ProductResponse updateStatus(Long id, boolean enabled) {
        Product product = find(id);
        product.setEnabled(enabled);
        return toResponse(repository.save(product));
    }

    @Transactional
    public ProductResponse deductInventory(InventoryDeductionRequest request) {
        Product product = find(request.productId());
        if (!product.isEnabled()) {
            throw new BadRequestException("Product is disabled");
        }
        if (product.getQuantity() < request.quantity()) {
            throw new BadRequestException("Insufficient inventory");
        }
        product.setQuantity(product.getQuantity() - request.quantity());
        return toResponse(repository.save(product));
    }

    private Product find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.isEnabled());
    }
}
