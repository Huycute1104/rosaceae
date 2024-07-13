package com.example.rosaceae.dto.Response.UserResponse;

import com.example.rosaceae.model.ShopPay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopPayResponse {
    private ShopPay shopPay;
    private String message;
    private int status;
}
