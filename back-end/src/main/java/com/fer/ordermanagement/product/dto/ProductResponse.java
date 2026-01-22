package com.fer.ordermanagement.product.dto;

import com.fer.ordermanagement.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ProductResponse {
    Long id;
    String sku;
    String name;
    BigDecimal price;
    String description;
    ProductStatus status;

    Long categoryId;
    String categoryName;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
