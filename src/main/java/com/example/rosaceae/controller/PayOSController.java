package com.example.rosaceae.controller;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.example.rosaceae.service.PayOSService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PutMapping("/success/{id}")
    public ResponseEntity<PayOSSuccess> confirmOrderSuccess(@PathVariable int id) {
        return payOSService.Success(id);
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<PayOSCancel> confirmOrderCancel(@PathVariable int id) {
        return payOSService.Cancel(id);
    }
}
