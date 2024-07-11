package com.example.rosaceae.controller;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.example.rosaceae.service.PayOSService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payOS")
@RequiredArgsConstructor
public class PayOSController {
    @Autowired
    private PayOS payOS;

    @Autowired
    private PayOSService payOSService;

    @PostMapping("/createOrderQR")
    public ResponseEntity<ObjectNode> createOrderQR(@RequestBody CreatePaymentLinkRequestBody body) {
        return payOSService.createOrderQR(body);
    }
    @GetMapping("/success")
    public ResponseEntity<PayOSSuccess> confirmOrderSuccess(@RequestParam int orderCode) {
        try {
            return payOSService.Success(orderCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<PayOSCancel> confirmOrderCancel(@RequestParam int orderCode) {
        try {
            return payOSService.Cancel(orderCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
