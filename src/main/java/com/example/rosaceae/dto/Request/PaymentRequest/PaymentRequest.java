package com.example.rosaceae.dto.Request.PaymentRequest;

import lombok.Data;

@Data
public class PaymentRequest {
    int orderTotal;
    String orderInfo;
}
