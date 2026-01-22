package com.fer.ordermanagement.category.dto;

import com.fer.ordermanagement.category.enums.CategoryStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CategoryResponse {
    Long id;
    String name;
    String description;
    CategoryStatus status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
