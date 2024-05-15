package com.example.rosaceae.controller;

import com.example.rosaceae.dto.PaymentRequest;
import com.example.rosaceae.dto.RefundRequest;
import com.example.rosaceae.service.VNPAYService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPAYService vnpayService;

    @PostMapping("/billing")
    public ResponseEntity<String> payment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request){
        String baseUrl = "https://google.com";
        String vnpayUrl = vnpayService.createOrder(paymentRequest.getOrderTotal(), paymentRequest.getOrderInfo(), baseUrl);
        return ResponseEntity.ok(vnpayUrl);
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestBody RefundRequest refundRequest, HttpServletRequest request) throws ServletException, IOException {
        vnpayService.refund(refundRequest, request);
        return ResponseEntity.ok("refund success");
    }
}
