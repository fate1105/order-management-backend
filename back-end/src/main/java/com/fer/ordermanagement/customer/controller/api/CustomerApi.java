package com.fer.ordermanagement.customer.controller.api;

import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.customer.dto.CustomerRequest;
import com.fer.ordermanagement.customer.dto.CustomerResponse;
import com.fer.ordermanagement.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customers", description = "Quản lý khách hàng")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/customers")
public interface CustomerApi {

    @Operation(summary = "Tạo khách hàng mới")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Email hoặc số điện thoại đã tồn tại")
    })
    @PostMapping
    ResponseEntity<BaseResponse<CustomerResponse>> create(
            @Valid @RequestBody CustomerRequest request
    );

    @Operation(summary = "Cập nhật khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng"),
            @ApiResponse(responseCode = "409", description = "Email hoặc số điện thoại đã tồn tại")
    })
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    );

    @Operation(summary = "Lấy khách hàng theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy khách hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<CustomerResponse>> getById(
            @PathVariable Long id
    );

    @Operation(summary = "Lấy danh sách khách hàng", description = "Hỗ trợ filter theo keyword")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    ResponseEntity<BaseResponse<Page<CustomerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Xóa khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng"),
            @ApiResponse(responseCode = "409", description = "Khách hàng đang có đơn hàng")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable Long id
    );

    @Operation(summary = "Lấy lịch sử đơn hàng của khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @GetMapping("/{id}/orders")
    ResponseEntity<BaseResponse<List<OrderResponse>>> getOrderHistory(
            @PathVariable Long id
    );
}