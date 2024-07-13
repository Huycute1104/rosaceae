package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.UserRequest.UserBankRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserBankResponse;
import com.example.rosaceae.service.UserBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
public class UserBankController {
    @Autowired
    private UserBankService userBankService;

    @PostMapping("/{userId}")
    public UserBankResponse addUserBank(@PathVariable int userId, @RequestBody UserBankRequest userBankRequest) {
        return userBankService.addUserBank(userId, userBankRequest);
    }
}
