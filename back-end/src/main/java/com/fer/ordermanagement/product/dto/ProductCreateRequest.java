package com.fer.ordermanagement.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductCreateRequest {
    @NotBlank(message = "Sku must not be blank!")
    @Size(max = 60)
    String sku;

    @NotBlank(message = "Name must not be blank!")
    @Size(max = 160)
    String name;

    @NotNull(message = "Price must not be null!")
    @Positive(message = "Price > 0")
    BigDecimal price;

    String description;

    @NotNull(message = "Category Id must not be null")
    Long categoryId;
}
