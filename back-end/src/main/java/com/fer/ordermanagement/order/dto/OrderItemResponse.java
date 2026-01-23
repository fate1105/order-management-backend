package com.fer.ordermanagement.order.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemResponse {
    Long productId;

    String productName;

    String productSku;

    BigDecimal price;

    Integer quantity;

    BigDecimal subtotal;
}
