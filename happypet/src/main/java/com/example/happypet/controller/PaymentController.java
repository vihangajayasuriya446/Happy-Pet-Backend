package com.example.happypet.controller;


import com.example.happypet.model.EzCashRequest;
import com.example.happypet.model.PaymentRequest;
import com.example.happypet.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.happypet.model.Payment;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/card")
    public ResponseEntity<String> processCardPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.processCardPayment(paymentRequest);
    }

    @GetMapping("/get-balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam String phone) {
        return paymentService.getBalance(phone);
    }

    @PostMapping("/ezcash")
    public ResponseEntity<String> processEzCashPayment(@RequestBody EzCashRequest request) {
        return paymentService.processEzCashPayment(request);
    }


}
