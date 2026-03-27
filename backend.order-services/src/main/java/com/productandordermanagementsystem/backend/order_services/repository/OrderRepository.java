package com.productandordermanagementsystem.backend.order_services.repository;

import com.productandordermanagementsystem.backend.order_services.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "items")
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = "items")
    List<Order> findAllByOrderByCreatedAtDesc();
}
