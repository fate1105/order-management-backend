package com.fer.ordermanagement.category.repository;

import com.fer.ordermanagement.category.entity.Category;
import com.fer.ordermanagement.category.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStatus(CategoryStatus status);
    List<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
