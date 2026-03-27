package com.productandordermanagementsystem.backend.order_services.controller;

import com.productandordermanagementsystem.backend.order_services.dto.cart.AddCartItemRequest;
import com.productandordermanagementsystem.backend.order_services.dto.cart.CartResponse;
import com.productandordermanagementsystem.backend.order_services.dto.cart.UpdateCartItemRequest;
import com.productandordermanagementsystem.backend.order_services.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get-items")
    public ResponseEntity<CartResponse> getCart() { return ResponseEntity.ok(cartService.getCart()); }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddCartItemRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.addItem(request, httpServletRequest.getHeader("Authorization")));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.updateItem(cartItemId, request, httpServletRequest.getHeader("Authorization")));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
