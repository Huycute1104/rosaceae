package com.example.rosaceae.service;


import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> getAllUser(Pageable pageable);
    UserResponse toggleUserStatus(int userId);
}
