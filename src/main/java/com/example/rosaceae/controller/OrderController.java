package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.OrderDetailRequest.OrderDetailRequest;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.OrderDetailResponse.OrderDetailResponse;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.service.OrderDetailService;
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
    @Autowired
    private OrderDetailService  orderDetailService;

    @PostMapping("")
//    @PreAuthorize("hasAuthority('customer:create')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request ){
        return ResponseEntity.ok(orderService.createOrderWithDetails(request));
    }

    @PostMapping("/orderdetail")
//    @PreAuthorize("hasAuthority('customer:create')")
    public ResponseEntity<OrderDetailResponse> createOrderDetail(@RequestBody OrderDetailRequest request ){
        return ResponseEntity.ok(orderDetailService.createOrderDetail(request));
    }
}
