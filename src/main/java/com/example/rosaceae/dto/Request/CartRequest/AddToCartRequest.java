package com.example.rosaceae.dto.Request.CartRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private int userId;
    private int itemId;
//    private int quantity;
}