package com.fer.ordermanagement.category.dto;

import com.fer.ordermanagement.category.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryRequest {
    @NotBlank(message = "Name must not be blank!")
    @Size(max = 120)
    String name;

    String description;

    @NotNull(message = "Status must not be null!")
    CategoryStatus status;
}
