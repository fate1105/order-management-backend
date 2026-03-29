package com.fer.ordermanagement.category.controller.api;

import com.fer.ordermanagement.category.dto.CategoryRequest;
import com.fer.ordermanagement.category.dto.CategoryResponse;
import com.fer.ordermanagement.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "Quản lý danh mục sản phẩm")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/categories")
public interface CategoryApi {

    @Operation(summary = "Tạo danh mục mới")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping
    ResponseEntity<BaseResponse<CategoryResponse>> create(
            @Valid @RequestBody CategoryRequest req
    );

    @Operation(summary = "Cập nhật danh mục")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest req
    );

    @Operation(summary = "Lấy danh mục theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy danh mục"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<CategoryResponse>> getById(
            @PathVariable Long id
    );

    @Operation(summary = "Lấy tất cả danh mục")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    ResponseEntity<BaseResponse<List<CategoryResponse>>> getAll();

    @Operation(summary = "Xóa danh mục")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable Long id
    );
}