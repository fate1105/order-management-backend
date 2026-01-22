package com.fer.ordermanagement.inventory.repository;

import com.fer.ordermanagement.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);
}
