package com.fer.ordermanagement.payment.service;

import com.fer.ordermanagement.order.entity.Order;

public interface PaymentService {
    void createForOrder(Order order);
    void markSuccess(Long orderId);
    void markFailed(Long orderId);
}
