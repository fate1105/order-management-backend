package com.fer.ordermanagement.payment.service;

import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import com.fer.ordermanagement.inventory.service.InventoryService;
import com.fer.ordermanagement.order.entity.Order;
import com.fer.ordermanagement.order.enums.OrderStatus;
import com.fer.ordermanagement.payment.entity.Payment;
import com.fer.ordermanagement.payment.enums.PaymentMethod;
import com.fer.ordermanagement.payment.enums.PaymentStatus;
import com.fer.ordermanagement.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final InventoryService inventoryService;

    @Override
    public void createForOrder(Order order) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(PaymentMethod.COD);
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
    }

    @Override
    public void markSuccess(Long orderId) {
        Payment payment = paymentRepository.findByOrderIdForUpdate(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order: " + orderId));

        if (payment.getStatus() != PaymentStatus.PENDING)
            throw new ConflictException("Payment already processed for order: " + orderId);

        Order order = payment.getOrder();

        if (order.getStatus() != OrderStatus.CREATED)
            throw new ConflictException("Order is not in payable state: " + order.getStatus());

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setTransactionCode(generateTransactionCode());

        order.markPaid();
    }

    @Override
    public void markFailed(Long orderId) {
        Payment payment = paymentRepository.findByOrderIdForUpdate(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order: " + orderId));

        if (payment.getStatus() != PaymentStatus.PENDING)
            throw new ConflictException("Payment already processed for order: " + orderId);

        Order order = payment.getOrder();

        if (order.getStatus() != OrderStatus.CREATED)
            throw new ConflictException("Order cannot be cancelled in status: " + order.getStatus());

        payment.setStatus(PaymentStatus.FAILED);

        order.getItems().forEach(item ->
                inventoryService.release(
                        item.getProduct().getId(),
                        item.getQuantity()
                ));

        order.cancel();
    }

    private String generateTransactionCode() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
