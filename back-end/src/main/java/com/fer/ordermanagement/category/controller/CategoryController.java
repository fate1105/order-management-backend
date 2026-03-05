package com.fer.ordermanagement.category.controller;

import com.fer.ordermanagement.category.dto.CategoryRequest;
import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.category.service.CategoryService;
import com.fer.ordermanagement.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid @RequestBody CategoryRequest req
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.created(categoryService.create(req))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest req
    ){
        return ResponseEntity.ok(
                ApiResponse.success(categoryService.update(id, req))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
            ApiResponse.success(categoryService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll(){
        return ResponseEntity.ok(
                ApiResponse.success(categoryService.getAll())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success("Xóa danh mục sản phẩm thành công")
        );
    }
}
