package com.fer.ordermanagement.admin.dto.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStatusReportResponse {
    private String status;
    private Long count;
}