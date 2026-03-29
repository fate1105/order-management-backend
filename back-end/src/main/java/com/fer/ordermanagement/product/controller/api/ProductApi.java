package com.fer.ordermanagement.product.controller.api;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.product.dto.ProductCreateRequest;
import com.fer.ordermanagement.product.dto.ProductResponse;
import com.fer.ordermanagement.product.dto.ProductUpdateRequest;
import com.fer.ordermanagement.product.enums.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Products", description = "Quản lý sản phẩm")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/products")
public interface ProductApi {

    @Operation(summary = "Tạo sản phẩm mới")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping
    ResponseEntity<BaseResponse<ProductResponse>> create(
            @Valid @RequestBody ProductCreateRequest request
    );

    @Operation(summary = "Cập nhật sản phẩm")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm")
    })
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    );

    @Operation(summary = "Lấy sản phẩm theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy sản phẩm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm")
    })
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<ProductResponse>> getById(@PathVariable Long id);

    @Operation(summary = "Lấy danh sách sản phẩm", description = "Hỗ trợ filter theo keyword và status")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    ResponseEntity<BaseResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductStatus status
    );

    @Operation(summary = "Xóa sản phẩm")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id);
}