package com.fer.ordermanagement.order.dto;

import com.fer.ordermanagement.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class OrderResponse {
    Long orderId;

    String orderCode;

    OrderStatus status;

    BigDecimal totalAmount;

    LocalDateTime createdAt;

    List<OrderItemResponse> items;
}
