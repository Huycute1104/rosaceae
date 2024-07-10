package com.example.rosaceae.controller;


import com.example.rosaceae.dto.Request.UserRequest.ShopRequest;
import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.auth.AuthenticationResponse;
import com.example.rosaceae.dto.Response.UserResponse.ShopResponse;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
//    @PreAuthorize("hasAuthority('admin:read')")
    public Page<User> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100000") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUser(pageable);
    }
    @PutMapping("/toggle-status/{userId}")
//    @PreAuthorize("hasAuthority('admin:update')")
    public UserResponse toggleUserStatus(@PathVariable int userId) {
        return userService.toggleUserStatus(userId);
    }

    @PutMapping("/update")
//    @PreAuthorize("hasAuthority('user:write') or hasAuthority('admin:write')")
    public UserResponse updateUserDetails(@RequestBody UserRequest updateUserRequest) {
        return userService.updateUserDetails(updateUserRequest);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<User> searchByAccountNameOrPhone(@RequestParam String keyword) {
        return userService.searchByAccountNameOrPhone(keyword);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<User> getCategoryByID(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUser/{token}")
    @PreAuthorize("hasAuthority('shop:read')")
    public Optional<User> getUserByToken(@PathVariable String token) {
        return userService.getUserByToken(token);
    }

    @GetMapping("/get-user-by-email")
    public AuthenticationResponse getUserByEmail(@RequestParam(name = "email") String email) {
        return userService.getUserByEmail(email);
    }
    @PostMapping("/create-shop")
    public ResponseEntity<ShopResponse> createShop(@RequestBody ShopRequest shopRequest) {
        ShopResponse response = userService.createShop(shopRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/changeCoverImage")
    public ResponseEntity<ShopResponse> changeCoverImages(
            @RequestParam("shopId") int shopId,
            @RequestParam("coverImage") MultipartFile coverImage) {
        ShopResponse response = userService.changeCoverImages(shopId, coverImage);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
