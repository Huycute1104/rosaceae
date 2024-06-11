package com.example.rosaceae.dto.Request.OrderRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private float total;
    private int voucherId;
    private int customerId;
}
