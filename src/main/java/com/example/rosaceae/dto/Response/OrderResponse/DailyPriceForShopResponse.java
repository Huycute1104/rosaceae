package com.example.rosaceae.dto.Response.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyPriceForShopResponse {
    private int day;
    private double totalPriceForShop;
}