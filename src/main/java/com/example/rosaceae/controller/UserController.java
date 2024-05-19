package com.example.rosaceae.controller;


import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public Page<User> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUser(pageable);
    }
    @PutMapping("/toggle-status/{userId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public UserResponse toggleUserStatus(@PathVariable int userId) {
        return userService.toggleUserStatus(userId);
    }

    @PutMapping("/update")
//    @PreAuthorize("hasAuthority('user:write') or hasAuthority('admin:write')")
    public UserResponse updateUserDetails(@RequestBody UserRequest updateUserRequest) {
        return userService.updateUserDetails(updateUserRequest);
    }

}
