package com.example.rosaceae.service;


import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<User> getAllUser(Pageable pageable);

    boolean emailVerify(String token);
    UserResponse toggleUserStatus(int userId);
    UserResponse updateUserDetails(UserRequest updateUserRequest);
    List<User> searchByAccountNameOrPhone(String keyword);
    Optional<User> getUserById(int userId);
}
