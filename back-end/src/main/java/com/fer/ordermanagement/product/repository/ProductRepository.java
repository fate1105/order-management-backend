package com.fer.ordermanagement.product.repository;

import com.fer.ordermanagement.product.entity.Product;
import com.fer.ordermanagement.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByNameContainingIgnoreCase(String name);
}
