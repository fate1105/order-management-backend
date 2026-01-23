package com.fer.ordermanagement.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderItemRequest {
    @NotNull(message = "Product id must not be null")
    Long productId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity;
}
