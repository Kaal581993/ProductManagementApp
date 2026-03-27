package com.productandordermanagementsystem.backend.order_services.repository;

import com.productandordermanagementsystem.backend.order_services.entity.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCartUserId(Long id, Long userId);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
