package com.example.rosaceae.service;


import com.example.rosaceae.dto.Data.UserDTO;
import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<User> getAllUser(Pageable pageable);
    boolean emailVerify(String token);
    UserResponse toggleUserStatus(int userId);
    UserResponse updateUserDetails(UserRequest updateUserRequest);
    List<User> searchByAccountNameOrPhone(String keyword);
    Optional<User> getUserById(int userId);
    Optional<User> getUserByToken(String token);
    Page<User> getUser(Specification<User> spec, Pageable pageable);
    public Optional<UserDTO> getUserDTO(int userId, int page, int size);
}
