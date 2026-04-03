package com.fer.ordermanagement.admin.service;

import com.fer.ordermanagement.admin.dto.report.*;
import com.fer.ordermanagement.customer.service.CustomerService;
import com.fer.ordermanagement.order.repository.OrderItemRepository;
import com.fer.ordermanagement.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerService customerService;

    // Cache 10 phút — key theo startDate+endDate
    @Cacheable(value = "report:revenue", key = "#startDate + '_' + #endDate")
    public List<RevenueReportResponse> getRevenueReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return orderRepository.findRevenueByDateRange(start, end)
                .stream()
                .map(row -> RevenueReportResponse.builder()
                        .date((LocalDate) row[0])
                        .totalRevenue((BigDecimal) row[1])
                        .totalOrders((Long) row[2])
                        .build())
                .toList();
    }

    @Cacheable(value = "report:order-status")
    public List<OrderStatusReportResponse> getOrderStatusReport() {
        return orderRepository.countByStatus()
                .stream()
                .map(row -> OrderStatusReportResponse.builder()
                        .status(row[0].toString())
                        .count((Long) row[1])
                        .build())
                .toList();
    }

    @Cacheable(value = "report:top-products", key = "#limit")
    public List<TopProductReportResponse> getTopProducts(int limit) {
        return orderItemRepository.findTopProducts(PageRequest.of(0, limit))
                .stream()
                .map(row -> TopProductReportResponse.builder()
                        .productId((Long) row[0])
                        .productName((String) row[1])
                        .sku((String) row[2])
                        .totalQuantitySold((Long) row[3])
                        .totalRevenue((BigDecimal) row[4])
                        .build())
                .toList();
    }

    // Customer history không cache vì riêng từng người
    public List<CustomerOrderHistoryResponse> getCustomerOrderHistory(Long customerId) {
        customerService.getCustomerEntityById(customerId); // validate tồn tại

        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(order -> CustomerOrderHistoryResponse.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus().name())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }
}