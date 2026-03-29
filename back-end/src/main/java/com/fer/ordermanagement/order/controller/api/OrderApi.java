package com.fer.ordermanagement.order.controller.api;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import com.fer.ordermanagement.order.dto.OrderRequest;
import com.fer.ordermanagement.order.dto.OrderResponse;
import com.fer.ordermanagement.order.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Quản lý đơn hàng")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/orders")
public interface OrderApi {

    @Operation(summary = "Tạo đơn hàng mới")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng hoặc sản phẩm")
    })
    @PostMapping
    ResponseEntity<BaseResponse<OrderResponse>> create(
            @Valid @RequestBody OrderRequest req
    );

    @Operation(summary = "Lấy đơn hàng theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy đơn hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn hàng")
    })
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<OrderResponse>> getById(
            @PathVariable Long id
    );

    @Operation(summary = "Lấy danh sách đơn hàng", description = "Hỗ trợ filter theo keyword và status")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    ResponseEntity<BaseResponse<PageResponse<OrderResponse>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status
    );

    @Operation(summary = "Hủy đơn hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hủy thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn hàng")
    })
    @PatchMapping("/{id}/cancel")
    ResponseEntity<BaseResponse<Void>> cancel(
            @PathVariable Long id
    );
}