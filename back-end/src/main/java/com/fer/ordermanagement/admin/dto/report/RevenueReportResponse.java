package com.fer.ordermanagement.admin.dto.report;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class RevenueReportResponse {
    private LocalDate date;
    private BigDecimal totalRevenue;
    private Long totalOrders;
}