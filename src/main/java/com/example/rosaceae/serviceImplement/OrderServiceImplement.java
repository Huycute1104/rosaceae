package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.model.Order;
import com.example.rosaceae.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImplement implements OrderService {
    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }
}
