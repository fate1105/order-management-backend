package com.fer.ordermanagement.inventory.entity;

import com.fer.ordermanagement.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@NoArgsConstructor

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "reserved_quantity",nullable = false)
    private Integer reservedQuantity = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    public static Inventory create(Product product) {
        Inventory inv = new Inventory();
        inv.product = product;
        inv.quantity = 0;
        inv.reservedQuantity = 0;
        return inv;
    }

    public void increase(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        this.quantity += amount;
    }

    public void reserve(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (getAvailable() < amount) {
            throw new IllegalStateException("Not enough stock");
        }
        this.reservedQuantity += amount;
    }

    public void release(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (reservedQuantity < amount) {
            throw new IllegalStateException("Release amount exceeds reserved");
        }
        this.reservedQuantity -= amount;
    }

    public int getAvailable() {
        return quantity - reservedQuantity;
    }
}
