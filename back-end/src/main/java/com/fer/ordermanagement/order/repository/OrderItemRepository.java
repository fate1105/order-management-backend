package com.fer.ordermanagement.order.repository;

import com.fer.ordermanagement.order.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT oi.product.id,
               oi.product.name,
               oi.product.sku,
               SUM(oi.quantity) as totalQty,
               SUM(oi.subtotal) as totalRevenue
        FROM OrderItem oi
        JOIN oi.order o
        WHERE o.status = 'COMPLETED'
        GROUP BY oi.product.id, oi.product.name, oi.product.sku
        ORDER BY totalQty DESC
        """)
    List<Object[]> findTopProducts(Pageable pageable);
}
