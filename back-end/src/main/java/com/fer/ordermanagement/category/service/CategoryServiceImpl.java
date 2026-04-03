package com.fer.ordermanagement.category.service;

import com.fer.ordermanagement.audit.service.AuditLogService;
import com.fer.ordermanagement.category.dto.CategoryRequest;
import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.category.entity.Category;
import com.fer.ordermanagement.category.mapper.CategoryMapper;
import com.fer.ordermanagement.category.repository.CategoryRepository;
import com.fer.ordermanagement.common.exception.ConflictException;
import com.fer.ordermanagement.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest req){
        if (categoryRepository.existsByNameIgnoreCase(req.getName())){
            throw new ConflictException("Category name already exists!" + req.getName());
        }

        Category category = new Category();
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setStatus(req.getStatus());

        categoryRepository.save(category);
        auditLogService.log("CREATE", "CATEGORY", category.getId());
        return CategoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found:" + id));

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(req.getName(), id)) {
            throw new ConflictException("Category name already exists!");
        }
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setStatus(req.getStatus());

        categoryRepository.save(category);
        auditLogService.log("UPDATE", "CATEGORY", id);
        return CategoryMapper.toResponse(category);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryResponse getById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found:" + id));
        return CategoryMapper.toResponse(category);
    }

    @Cacheable(value = "categories")
    @Override
    public List<CategoryResponse> getAll(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new NotFoundException("Category not found:" + id);
        }
        categoryRepository.deleteById(id);
        auditLogService.log("DELETE", "CATEGORY", id);
    }

    @Override
    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found:" + id));
    }
}
