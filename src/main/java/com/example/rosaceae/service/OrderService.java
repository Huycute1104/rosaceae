package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.OrderDTO;
import com.example.rosaceae.dto.Data.OrderDetailDTO;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    public Page<Order> findAll(Pageable pageable);
    public OrderResponse CreateOrder(CreateOrderRequest request);
    public OrderResponse createOrderWithDetails(CreateOrderRequest request);
    Page<OrderDetailDTO> getOrderDetailsByItemUserId(int userId, Pageable pageable);
    Page<OrderDTO> getOrderForCustomer(int id, Pageable pageable);
    long countOrdersByUserId(int userId);
}
