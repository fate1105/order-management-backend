package com.fer.ordermanagement.product.service;

import com.fer.ordermanagement.product.dto.CategoryRequest;
import com.fer.ordermanagement.product.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest req);
    CategoryResponse update(Long id, CategoryRequest req);
    CategoryResponse getById(Long id);
    List<CategoryResponse> getAll();
    void delete(Long id);
}
