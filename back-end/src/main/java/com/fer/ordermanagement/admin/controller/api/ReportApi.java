package com.fer.ordermanagement.admin.controller.api;

import com.fer.ordermanagement.admin.dto.report.*;
import com.fer.ordermanagement.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Admin - Báo cáo & Thống kê", description = "Các API báo cáo doanh thu, đơn hàng và sản phẩm")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/admin/reports")
public interface ReportApi {

    @Operation(summary = "Báo cáo doanh thu theo khoảng thời gian")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Ngày không hợp lệ")
    })
    @GetMapping("/revenue")
    ResponseEntity<BaseResponse<List<RevenueReportResponse>>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );

    @Operation(summary = "Thống kê số lượng đơn hàng theo trạng thái")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping("/orders/status")
    ResponseEntity<BaseResponse<List<OrderStatusReportResponse>>> getOrderStatus();

    @Operation(summary = "Top sản phẩm bán chạy nhất", description = "Mặc định lấy top 10")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping("/products/top")
    ResponseEntity<BaseResponse<List<TopProductReportResponse>>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit
    );

    @Operation(summary = "Lịch sử đơn hàng của khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @GetMapping("/customers/{customerId}/orders")
    ResponseEntity<BaseResponse<List<CustomerOrderHistoryResponse>>> getCustomerHistory(
            @PathVariable Long customerId
    );
}