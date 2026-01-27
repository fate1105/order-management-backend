package com.fer.ordermanagement.payment.controller;

import com.fer.ordermanagement.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // ================= PAY SUCCESS (MOCK CALLBACK) =================
    @PostMapping("/{orderId}/success")
    public ResponseEntity<Void> paySuccess(
            @PathVariable Long orderId
    ) {
        paymentService.markSuccess(orderId);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================= PAY FAILED / CANCEL =================
    @PostMapping("/{orderId}/fail")
    public ResponseEntity<Void> payFailed(
            @PathVariable Long orderId
    ) {
        paymentService.markFailed(orderId);
        return ResponseEntity.noContent().build(); // 204
    }
}