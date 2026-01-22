package com.fer.ordermanagement.product.mapper;

import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.entity.Product;

public class ProductMapper {
    public static ProductResponse toResponse(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .status(product.getStatus())
                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )
                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory().getName()
                                : null
                )
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
