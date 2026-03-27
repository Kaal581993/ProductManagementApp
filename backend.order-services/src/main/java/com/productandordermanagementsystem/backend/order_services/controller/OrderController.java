package com.productandordermanagementsystem.backend.order_services.controller;

import com.productandordermanagementsystem.backend.order_services.dto.order.OrderResponse;
import com.productandordermanagementsystem.backend.order_services.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.placeOrder(
                        request.getHeader("Authorization")
                )
                );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(HttpServletRequest request) {

        return ResponseEntity.ok(
                orderService.getOrders(request.getHeader("Authorization"))
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(HttpServletRequest request) {
        return ResponseEntity.ok(
                orderService.getAllOrders(request.getHeader("Authorization"))
        );
    }
}
