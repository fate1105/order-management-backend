package com.fer.ordermanagement.order.repository;

import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long id);

    boolean existsByCustomerId(Long customerId);

    @Query(value = """
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.items i
        JOIN FETCH i.product
        WHERE
            (:keyword IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:status IS NULL OR o.status = :status)
    """,
            countQuery = """
        SELECT COUNT(DISTINCT o)
        FROM Order o
        JOIN o.items i
        WHERE
            (:keyword IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:status IS NULL OR o.status = :status)
    """
    )
    Page<Order> searchWithPaging(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
