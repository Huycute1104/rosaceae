package com.example.rosaceae.controller;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.service.PayOSService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pay")
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
}
