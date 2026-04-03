package com.fer.ordermanagement.admin.controller;

import com.fer.ordermanagement.admin.controller.api.ReportApi;
import com.fer.ordermanagement.admin.dto.report.*;
import com.fer.ordermanagement.admin.service.ReportService;
import com.fer.ordermanagement.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @Override
    public ResponseEntity<BaseResponse<List<RevenueReportResponse>>> getRevenue(
            LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(BaseResponse.success(
                reportService.getRevenueReport(startDate, endDate)));
    }

    @Override
    public ResponseEntity<BaseResponse<List<OrderStatusReportResponse>>> getOrderStatus() {
        return ResponseEntity.ok(BaseResponse.success(
                reportService.getOrderStatusReport()));
    }

    @Override
    public ResponseEntity<BaseResponse<List<TopProductReportResponse>>> getTopProducts(int limit) {
        return ResponseEntity.ok(BaseResponse.success(
                reportService.getTopProducts(limit)));
    }

    @Override
    public ResponseEntity<BaseResponse<List<CustomerOrderHistoryResponse>>> getCustomerHistory(
            Long customerId) {
        return ResponseEntity.ok(BaseResponse.success(
                reportService.getCustomerOrderHistory(customerId)));
    }
}