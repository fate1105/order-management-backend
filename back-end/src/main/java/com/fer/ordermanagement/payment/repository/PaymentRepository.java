package com.fer.ordermanagement.payment.repository;

import com.fer.ordermanagement.payment.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM Payment p
            WHERE p.order.id = :orderId
    """)
    Optional<Payment> findByOrderIdForUpdate(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);
}
