package com.productandordermanagementsystem.backend.product_management.repository;

import com.productandordermanagementsystem.backend.product_management.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByEnabledTrueOrderByIdAsc();
}
