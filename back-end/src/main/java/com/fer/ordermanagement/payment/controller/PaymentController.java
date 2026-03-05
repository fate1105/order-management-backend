package com.fer.ordermanagement.payment.controller;

import com.fer.ordermanagement.common.response.ApiResponse;
import com.fer.ordermanagement.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/{orderId}/success")
    public ResponseEntity<ApiResponse<Void>> paySuccess(
            @PathVariable Long orderId
    ) {
        paymentService.markSuccess(orderId);
        return ResponseEntity.ok(
                ApiResponse.success("Thanh toán thành công")
        );
    }

    @PostMapping("/{orderId}/fail")
    public ResponseEntity<ApiResponse<Void>> payFailed(
            @PathVariable Long orderId
    ) {
        paymentService.markFailed(orderId);
        return ResponseEntity.ok(
                ApiResponse.success("Thanh toán thất bại")
        );
    }
}