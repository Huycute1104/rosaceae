package com.example.rosaceae.dto.Response.CartResponse;

import com.example.rosaceae.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String status;
    private Cart cart;
}