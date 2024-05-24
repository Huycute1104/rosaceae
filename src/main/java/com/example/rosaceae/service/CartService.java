package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.model.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {
    public CartResponse addToCart(AddToCartRequest request);
    Page<Cart> viewCarOfCustomer(int customerId, int type,Pageable pageable);
    public CartResponse removeCart(int id);
}
