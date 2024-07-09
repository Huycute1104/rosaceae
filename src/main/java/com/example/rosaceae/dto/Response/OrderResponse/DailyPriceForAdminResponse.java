package com.example.rosaceae.dto.Response.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyPriceForAdminResponse {
    private int day;
    private double totalPriceForAdmin;
}