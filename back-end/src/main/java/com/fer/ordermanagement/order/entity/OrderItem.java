package com.fer.ordermanagement.order.entity;

import com.fer.ordermanagement.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static OrderItem create(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.order = order;
        item.product = product;
        item.quantity = quantity;
        item.price = product.getPrice();
        item.subtotal = item.price.multiply(BigDecimal.valueOf(quantity));
        return item;
    }
}
