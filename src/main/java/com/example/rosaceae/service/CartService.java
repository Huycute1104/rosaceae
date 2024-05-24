package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;

public interface CartService {
    public CartResponse addToCart(AddToCartRequest request);
}
