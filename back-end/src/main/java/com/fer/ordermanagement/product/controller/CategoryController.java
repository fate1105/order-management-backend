package com.fer.ordermanagement.product.controller;

import com.fer.ordermanagement.product.dto.CategoryRequest;
import com.fer.ordermanagement.product.dto.CategoryResponse;
import com.fer.ordermanagement.product.service.CategoryService;
import com.fer.ordermanagement.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest req
    ){
        CategoryResponse res = categoryService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest req
    ){
        return ResponseEntity.ok(categoryService.update(id, req));
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id){
        return categoryService.getById(id);
    }

    @GetMapping
    public List<CategoryResponse> getAll(){
        return categoryService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
