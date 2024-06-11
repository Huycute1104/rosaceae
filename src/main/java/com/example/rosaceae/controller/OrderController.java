package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
//    @PreAuthorize("hasAuthority('customer:create')")
    public ResponseEntity<OrderResponse> createCategory(@RequestBody CreateOrderRequest request ){
        return ResponseEntity.ok(orderService.CreateOrder(request));
    }
}
