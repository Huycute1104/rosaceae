package com.example.rosaceae.dto.Request.OrderDetailRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    private int customerId;
    private int orderId;
}
