package com.fer.ordermanagement.product.mapper;

import com.fer.ordermanagement.product.dto.CategoryResponse;
import com.fer.ordermanagement.product.entity.Category;

public class CategoryMapper {
    public static CategoryResponse toResponse(Category category){
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
