package com.example.rosaceae.service;

import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopService {
    Page<User> getAllUser(Pageable pageable);
}
