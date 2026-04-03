package com.fer.ordermanagement.order.repository;

import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.items i
        JOIN FETCH i.product
        WHERE o.customer.id = :customerId
        ORDER BY o.createdAt DESC
    """)
    List<Order> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId);

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
    // Doanh thu theo ngày
    @Query("""
    SELECT CAST(o.createdAt AS LocalDate) as date,
           SUM(o.totalAmount) as totalRevenue,
           COUNT(o) as totalOrders
    FROM Order o
    WHERE o.status = 'COMPLETED'
      AND o.createdAt BETWEEN :start AND :end
    GROUP BY CAST(o.createdAt AS LocalDate)
    ORDER BY CAST(o.createdAt AS LocalDate)
    """)
    List<Object[]> findRevenueByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Đơn hàng theo trạng thái
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countByStatus();

    boolean existsByCustomerId(Long customerId);
}
