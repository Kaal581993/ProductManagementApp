package com.productandordermanagementsystem.backend.order_services.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }

    public void setCart(Cart cart) { this.cart = cart; }

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public java.math.BigDecimal getUnitPrice() { return unitPrice; }

    public void setUnitPrice(java.math.BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
