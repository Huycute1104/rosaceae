package com.example.rosaceae.dto.Request.OrderRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private float total;
    private int voucherId;
    private int customerId;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private int itemId;
        private int quantity;
    }
}
