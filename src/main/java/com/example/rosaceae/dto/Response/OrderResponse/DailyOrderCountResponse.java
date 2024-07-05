package com.example.rosaceae.dto.Response.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyOrderCountResponse {
    private int day;
    private long count;
}