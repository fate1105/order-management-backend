package com.fer.ordermanagement.admin.dto.report;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TopProductReportResponse {
    private Long productId;
    private String productName;
    private String sku;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
}