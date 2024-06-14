package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.OrderDTO;
import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.OrderDetailRequest.OrderDetailRequest;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.OrderDetailResponse.OrderDetailResponse;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.model.Order;
import com.example.rosaceae.service.OrderDetailService;
import com.example.rosaceae.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/customer/{id}")
    public Page<OrderDTO> getOrdersForCustomer(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrderForCustomer(id, pageable);
    }
}
