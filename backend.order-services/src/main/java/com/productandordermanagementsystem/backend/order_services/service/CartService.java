package com.productandordermanagementsystem.backend.order_services.service;

import com.productandordermanagementsystem.backend.order_services.client.ProductClient;
import com.productandordermanagementsystem.backend.order_services.dto.cart.AddCartItemRequest;
import com.productandordermanagementsystem.backend.order_services.dto.cart.CartItemResponse;
import com.productandordermanagementsystem.backend.order_services.dto.cart.CartResponse;
import com.productandordermanagementsystem.backend.order_services.dto.cart.UpdateCartItemRequest;
import com.productandordermanagementsystem.backend.order_services.dto.product.ProductSnapshot;
import com.productandordermanagementsystem.backend.order_services.entity.Cart;
import com.productandordermanagementsystem.backend.order_services.entity.CartItem;
import com.productandordermanagementsystem.backend.order_services.exception.BadRequestException;
import com.productandordermanagementsystem.backend.order_services.exception.ResourceNotFoundException;
import com.productandordermanagementsystem.backend.order_services.repository.CartItemRepository;
import com.productandordermanagementsystem.backend.order_services.repository.CartRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CurrentUserService currentUserService;
    private final ProductClient productClient;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CurrentUserService currentUserService, ProductClient productClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.currentUserService = currentUserService;
        this.productClient = productClient;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        return toResponse(getOrCreateCart());
    }

    @Transactional
    public CartResponse addItem(AddCartItemRequest request, String authHeader) {
        Cart cart = getOrCreateCart();
        ProductSnapshot product = productClient.getProduct(request.productId(), authHeader);
        validateProduct(product, request.quantity());
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId()).orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(product.id());
            newItem.setProductName(product.name());
            newItem.setQuantity(0);
            newItem.setUnitPrice(product.price());
            return newItem;
        });
        int newQuantity = item.getQuantity() + request.quantity();
        validateProduct(product, newQuantity);
        item.setQuantity(newQuantity);
        item.setUnitPrice(product.price());
        cartItemRepository.save(item);
        return toResponse(getOrCreateCart());
    }

    @Transactional
    public CartResponse updateItem(Long cartItemId, UpdateCartItemRequest request, String authHeader) {
        CartItem item = cartItemRepository.findByIdAndCartUserId(cartItemId, currentUserService.currentUser().userId()).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        ProductSnapshot product = productClient.getProduct(item.getProductId(), authHeader);
        validateProduct(product, request.quantity());
        item.setQuantity(request.quantity());
        item.setUnitPrice(product.price());
        cartItemRepository.save(item);
        return toResponse(getOrCreateCart());
    }

    @Transactional
    public void removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findByIdAndCartUserId(cartItemId, currentUserService.currentUser().userId()).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public Cart getOrCreateCart() {
        return cartRepository.findByUserId(currentUserService.currentUser().userId()).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(currentUserService.currentUser().userId());
            return cartRepository.save(cart);
        });
    }

    private void validateProduct(ProductSnapshot product, int quantity) {
        if (product == null || !product.enabled()) {
            throw new BadRequestException("Product is unavailable");
        }
        if (product.quantity() < quantity) {
            throw new BadRequestException("Requested quantity exceeds available inventory");
        }
    }

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(item -> new CartItemResponse(item.getId(), item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice(), item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))).toList();
        BigDecimal total = items.stream().map(CartItemResponse::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(cart.getId(), items, total);
    }
}
