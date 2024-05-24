package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('customer:create')")
    public CartResponse addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

}
