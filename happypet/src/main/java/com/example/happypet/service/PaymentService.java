package com.example.happypet.service;

import com.example.happypet.model.EzCashRequest;
import com.example.happypet.model.Payment;
import com.example.happypet.model.PaymentRequest;
import com.example.happypet.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final Map<String, Double> userBalances = new HashMap<>();

    @Autowired
    private PaymentRepository paymentRepository; // MySQL Database Repository

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        // Initialize EZ Cash user balances
        userBalances.put("0712345678", 100.0);
        userBalances.put("0723456789", 50.0);
        userBalances.put("5555555555", 5000.0);
        userBalances.put("1111111111", 1000.0);
    }

    // Process Card Payment & Save to MySQL
    public ResponseEntity<String> processCardPayment(PaymentRequest request) {
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        // Save payment to MySQL
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPhoneNumber("N/A"); // Not applicable for card payments
        payment.setPaymentMethod("Card");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        return ResponseEntity.ok("Card payment of $" + request.getAmount() + " successful");
    }

    // Get EZ Cash Balance
    public ResponseEntity<Map<String, Object>> getBalance(String phone) {
        Map<String, Object> response = new HashMap<>();
        response.put("balance", userBalances.getOrDefault(phone, 0.0));
        return ResponseEntity.ok(response);
    }

    // Process EZ Cash Payment & Save to MySQL
    public ResponseEntity<String> processEzCashPayment(EzCashRequest request) {
        if (!userBalances.containsKey(request.getPhone()) || userBalances.get(request.getPhone()) < request.getAmount()) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        }

        // Deduct balance
        userBalances.put(request.getPhone(), userBalances.get(request.getPhone()) - request.getAmount());

        // Save transaction to MySQL
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPhoneNumber(request.getPhone());
        payment.setPaymentMethod("EZ Cash");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        return ResponseEntity.ok("EZ Cash payment of $" + request.getAmount() + " successful");
    }

    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return ResponseEntity.ok(payments);
    }

}
