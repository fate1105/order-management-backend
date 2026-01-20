package com.fer.ordermanagement.product.dto;

import com.fer.ordermanagement.product.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateRequest {
    @NotBlank(message = "Name must not be blank!")
    @Size(max = 160)
    private String name;

    @NotNull(message = "Price must not be null!")
    @Positive(message = "Price > 0")
    private BigDecimal price;

    private String description;

    @NotNull(message = "Status must not be null!")
    private ProductStatus status;

    @NotNull(message = "CategoryId must not be null!")
    private Long categoryId;
}
