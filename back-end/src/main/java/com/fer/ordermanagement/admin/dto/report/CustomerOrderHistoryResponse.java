package com.fer.ordermanagement.admin.dto.report;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CustomerOrderHistoryResponse {
    private Long orderId;
    private String orderCode;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
}