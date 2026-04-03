package com.fer.ordermanagement.admin.controller;

import com.fer.ordermanagement.admin.dto.report.*;
import com.fer.ordermanagement.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueReportResponse>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getRevenueReport(startDate, endDate));
    }

    @GetMapping("/orders/status")
    public ResponseEntity<List<OrderStatusReportResponse>> getOrderStatus() {
        return ResponseEntity.ok(reportService.getOrderStatusReport());
    }

    @GetMapping("/products/top")
    public ResponseEntity<List<TopProductReportResponse>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reportService.getTopProducts(limit));
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<List<CustomerOrderHistoryResponse>> getCustomerHistory(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(reportService.getCustomerOrderHistory(customerId));
    }
}