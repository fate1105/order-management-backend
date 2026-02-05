package com.fer.ordermanagement.customer.dto;

import com.fer.ordermanagement.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class CustomerOrderResponse {
    Long orderId;

    String orderCode;

    OrderStatus status;

    BigDecimal totalAmount;

    LocalDateTime createdAt;
}
