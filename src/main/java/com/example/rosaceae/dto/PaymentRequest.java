package com.example.rosaceae.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    int orderTotal;
    String orderInfo;
}
