package com.example.rosaceae.dto.Response.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalPriceForAdminResponse {
    private float totalPriceForAdmin;
    private int month;
    private int year;
}