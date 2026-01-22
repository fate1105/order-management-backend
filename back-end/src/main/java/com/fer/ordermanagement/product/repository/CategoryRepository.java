package com.fer.ordermanagement.product.repository;

import com.fer.ordermanagement.product.entity.Category;
import com.fer.ordermanagement.product.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStatus(CategoryStatus status);
    List<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
