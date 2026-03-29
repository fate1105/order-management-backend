package com.fer.ordermanagement.category.controller;

import com.fer.ordermanagement.category.controller.api.CategoryApi;
import com.fer.ordermanagement.category.dto.CategoryRequest;
import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.category.service.CategoryService;
import com.fer.ordermanagement.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(categoryService.create(req))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> update(Long id, CategoryRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(categoryService.update(id, req))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> getById(Long id) {
        return ResponseEntity.ok(
                BaseResponse.success(categoryService.getById(id))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(
                BaseResponse.success(categoryService.getAll())
        );
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(
                BaseResponse.success("Xóa danh mục sản phẩm thành công")
        );
    }
}