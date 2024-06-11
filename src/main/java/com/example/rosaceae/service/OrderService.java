package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    public Page<Order> findAll(Pageable pageable);
    public OrderResponse CreateOrder(CreateOrderRequest request);
}
