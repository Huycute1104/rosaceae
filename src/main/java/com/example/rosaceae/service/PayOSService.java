package com.example.rosaceae.service;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;

public interface PayOSService {
    public ResponseEntity<ObjectNode> createOrderQR(CreatePaymentLinkRequestBody body);
    public ResponseEntity<PayOSSuccess> Success(int orderCode);
    public ResponseEntity<PayOSCancel> Cancel(int orderCode);
}
