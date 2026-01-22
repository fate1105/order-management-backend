package com.fer.ordermanagement.product.service;

import com.fer.ordermanagement.product.dto.CategoryRequest;
import com.fer.ordermanagement.product.dto.CategoryResponse;
import com.fer.ordermanagement.product.entity.Category;
import com.fer.ordermanagement.product.mapper.CategoryMapper;
import com.fer.ordermanagement.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest req){
        if (categoryRepository.existsByNameIgnoreCase(req.getName())){
            throw new RuntimeException("Category name already exists!" + req.getName());
        }

        Category category = new Category();
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setStatus(req.getStatus());

        categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot found category"));

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(req.getName(), id)) {
            throw new RuntimeException("Category name already exists!");
        }
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setStatus(req.getStatus());

        return CategoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse getById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        return CategoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAll(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found!");
        }
        categoryRepository.deleteById(id);
    }
}
