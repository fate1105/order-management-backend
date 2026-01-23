package com.fer.ordermanagement.order.entity;

import com.fer.ordermanagement.customer.entity.Customer;
import com.fer.ordermanagement.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", length = 50, nullable = false, unique = true)
    private String orderCode;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public static Order create(String orderCode, Customer customer) {
        Order order = new Order();
        order.orderCode = orderCode;
        order.customer = customer;
        order.status = OrderStatus.CREATED;
        order.totalAmount = BigDecimal.ZERO;
        return order;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotal();
    }

    private void recalcTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
