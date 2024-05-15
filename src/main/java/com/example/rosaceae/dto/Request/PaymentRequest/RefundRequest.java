package com.example.rosaceae.dto.Request.PaymentRequest;

import lombok.Data;

@Data
public class RefundRequest {
    String transtype = "02";
    String trans_date;
    String user;
    String order_id;
    String amount;
}
