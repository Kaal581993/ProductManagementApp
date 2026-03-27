package com.productandordermanagementsystem.backend.order_services.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productandordermanagementsystem.backend.order_services.client.ProductClient;
import com.productandordermanagementsystem.backend.order_services.client.UserClient;
import com.productandordermanagementsystem.backend.order_services.dto.order.OrderItemResponse;
import com.productandordermanagementsystem.backend.order_services.dto.order.OrderResponse;
import com.productandordermanagementsystem.backend.order_services.dto.user.UserSummary;
import com.productandordermanagementsystem.backend.order_services.entity.Cart;
import com.productandordermanagementsystem.backend.order_services.entity.CartItem;
import com.productandordermanagementsystem.backend.order_services.entity.Order;
import com.productandordermanagementsystem.backend.order_services.entity.OrderItem;
import com.productandordermanagementsystem.backend.order_services.entity.OrderStatus;
import com.productandordermanagementsystem.backend.order_services.event.OrderPlacedEvent;
import com.productandordermanagementsystem.backend.order_services.exception.BadRequestException;
import com.productandordermanagementsystem.backend.order_services.repository.CartItemRepository;
import com.productandordermanagementsystem.backend.order_services.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final CartService cartService;
    private final CurrentUserService currentUserService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String orderTopic;

    public OrderService(CartService cartService,
                        CurrentUserService currentUserService,
                        OrderRepository orderRepository,
                        CartItemRepository cartItemRepository,
                        ProductClient productClient,
                        UserClient userClient,
                        KafkaTemplate<String, String> kafkaTemplate,
                        ObjectMapper objectMapper,
                        @Value("${app.kafka.order-topic}") String orderTopic) {
        this.cartService = cartService;
        this.currentUserService = currentUserService;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.orderTopic = orderTopic;
    }

    @Transactional
    public OrderResponse placeOrder(String authHeader) {
        Cart cart = cartService.getOrCreateCart();
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        Order order = new Order();
        order.setUserId(currentUserService.currentUser().userId());
        order.setStatus(OrderStatus.PLACED);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            productClient.deductInventory(cartItem.getProductId(), cartItem.getQuantity(), authHeader);
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(cartItem.getProductId());
            item.setProductName(cartItem.getProductName());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getUnitPrice());
            BigDecimal lineTotal = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            item.setLineTotal(lineTotal);
            order.getItems().add(item);
            total = total.add(lineTotal);
        }
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getTotalAmount(),
                saved.getCreatedAt(),
                saved.getItems().stream()
                        .map(i -> new OrderPlacedEvent.OrderPlacedItem(i.getProductId(), i.getProductName(), i.getQuantity(), i.getLineTotal()))
                        .toList()
        );
        kafkaTemplate.send(orderTopic, String.valueOf(saved.getId()), toJson(event));
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        return toResponse(saved, null);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String authHeader) {
        if (ADMIN_ROLE.equals(currentUserService.currentUser().role())) {
            return getAllOrders(authHeader);
        }

        return orderRepository.findByUserIdOrderByCreatedAtDesc(currentUserService.currentUser().userId()).stream()
                .map(order -> toResponse(order, null))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(String authHeader) {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(order -> toResponse(order, resolveCustomerName(order.getUserId(), authHeader)))
                .toList();
    }

    private OrderResponse toResponse(Order order, String customerName) {
        return new OrderResponse(order.getId(), order.getUserId(), customerName, order.getStatus().name(), order.getTotalAmount(), order.getCreatedAt(), order.getItems().stream().map(i -> new OrderItemResponse(i.getId(), i.getProductId(), i.getProductName(), i.getQuantity(), i.getUnitPrice(), i.getLineTotal())).toList());
    }

    private String resolveCustomerName(Long userId, String authHeader) {
        UserSummary summary = userClient.getUserSummary(userId, authHeader);
        return summary == null ? null : summary.name();
    }

    private String toJson(OrderPlacedEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize order event", exception);
        }
    }
}
