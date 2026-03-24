package com.fer.ordermanagement.product.controller;

import com.fer.ordermanagement.common.response.ApiResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.product.enums.ProductStatus;
import com.fer.ordermanagement.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Products", description = "Quản lý sản phẩm")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Tạo sản phẩm mới")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.created(productService.create(request))
        );
    }

    @Operation(summary = "Cập nhật sản phẩm")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.update(id, request))
        );
    }

    @Operation(summary = "Lấy sản phẩm theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(productService.getById(id))
        );
    }

    @Operation(summary = "Lấy danh sách sản phẩm", description = "Hỗ trợ filter theo keyword và status")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductStatus status
    ) {
        Page<ProductResponse> result = productService.getAllPaged(page, size, keyword, status);
        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(result))
        );
    }

    @Operation(summary = "Xóa sản phẩm")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success("Xóa sản phẩm thành công")
        );
    }
}
