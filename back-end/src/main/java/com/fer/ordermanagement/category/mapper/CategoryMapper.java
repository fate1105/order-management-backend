package com.fer.ordermanagement.category.mapper;

import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.category.entity.Category;

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
