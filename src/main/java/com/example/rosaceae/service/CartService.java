package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.model.Cart;
import com.example.rosaceae.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    public CartResponse addToCart(AddToCartRequest request);
    Page<Cart> viewCarOfCustomer(int customerId, int type,Pageable pageable);
}
