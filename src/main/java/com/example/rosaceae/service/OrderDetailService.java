package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.OrderDetailRequest.OrderDetailRequest;
import com.example.rosaceae.dto.Response.OrderDetailResponse.OrderDetailResponse;

public interface OrderDetailService {
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request);
}
