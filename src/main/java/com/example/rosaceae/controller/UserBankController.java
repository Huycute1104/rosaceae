package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.UserRequest.UserBankRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserBankResponse;
import com.example.rosaceae.model.UserBank;
import com.example.rosaceae.service.UserBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
public class UserBankController {
    @Autowired
    private UserBankService userBankService;

    @PostMapping("/{userId}")
    public ResponseEntity<UserBankResponse> addUserBank(@PathVariable int userId, @RequestBody UserBankRequest userBankRequest) {
        UserBankResponse response = userBankService.addUserBank(userId, userBankRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/{bankId}")
    public ResponseEntity<UserBankResponse> updateUserBank(@PathVariable int bankId, @RequestBody UserBankRequest userBankRequest) {
        UserBankResponse response = userBankService.updateUserBank(bankId, userBankRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
    @GetMapping("bankAccount/{userId}")
    public ResponseEntity<List<UserBank>> getUserBanks(@PathVariable int userId) {
        List<UserBank> userBanks = userBankService.getUserBank(userId);
//        if (userBanks.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        return new ResponseEntity<>(userBanks, HttpStatus.OK);
    }
}
