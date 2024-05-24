package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Request.CartRequest.UpdateCartItem;
import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.model.Cart;
import com.example.rosaceae.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    @PreAuthorize("hasAuthority('customer:read')")
    public ResponseEntity<Page<Cart>> viewCartOfCustomer(
            @RequestParam int customerId,
            @RequestParam int type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (customerId <= 0 || type <= 0 || page < 0 || size <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Cart> carts = cartService.viewCarOfCustomer(customerId, type, pageable);

        if (carts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(carts);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('customer:delete')")
    public CartResponse removeFromCart(@PathVariable int id) {
        return cartService.removeCart(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('customer:update')")
    public ResponseEntity<CartResponse> updateUser(
            @PathVariable int id,
            @RequestBody UpdateCartItem request) {
        return ResponseEntity.ok(cartService.updateCartItem(id,request));
    }



}
