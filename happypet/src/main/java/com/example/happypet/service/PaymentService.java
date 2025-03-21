package com.example.happypet.service;


import com.example.happypet.model.EzCashRequest;
import com.example.happypet.model.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    private final Map<String, Double> userBalances = new HashMap<>();

    public PaymentService() {
        userBalances.put("0712345678", 100.0);
        userBalances.put("0723456789", 50.0);
    }

    public ResponseEntity<String> processCardPayment(PaymentRequest request) {
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }
        return ResponseEntity.ok("Card payment of $" + request.getAmount() + " successful");
    }

    public ResponseEntity<Map<String, Object>> getBalance(String phone) {
        Map<String, Object> response = new HashMap<>();
        response.put("balance", userBalances.getOrDefault(phone, 0.0));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> processEzCashPayment(EzCashRequest request) {
        if (!userBalances.containsKey(request.getPhone()) || userBalances.get(request.getPhone()) < request.getAmount()) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        }
        userBalances.put(request.getPhone(), userBalances.get(request.getPhone()) - request.getAmount());
        return ResponseEntity.ok("EZ Cash payment of $" + request.getAmount() + " successful");
    }
}
