package com.fer.ordermanagement.order.repository;

import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.items i
        JOIN FETCH i.product
    """)
    List<Order> findAllWithItems();

    @Query("""
        SELECT o
        FROM Order o
        JOIN FETCH o.items i
        JOIN FETCH i.product
        WHERE o.id = :id
    """)
    Optional<Order> findByIdWithItems(Long id);

    boolean existsByCustomerId(Long customerId);
}
