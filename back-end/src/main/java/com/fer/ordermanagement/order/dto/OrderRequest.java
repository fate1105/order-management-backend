package com.fer.ordermanagement.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderRequest {
    @NotNull(message = "Customer id must not be null")
    Long customerId;

    @NotEmpty(message = "Order must contain at least one item")
    @Size(max = 50, message = "Order cannot contain more than 50 items")
    @Valid
    List<OrderItemRequest> items;
}
