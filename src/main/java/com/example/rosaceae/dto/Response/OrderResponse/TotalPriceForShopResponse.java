package com.example.rosaceae.dto.Response.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalPriceForShopResponse {
    private float totalPriceForShop;
    private int month;
    private int year;
}