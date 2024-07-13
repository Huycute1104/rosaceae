package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.dto.Response.UserResponse.ShopPayResponse;
import com.example.rosaceae.service.ShopPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private ShopPayService shopPayService;

    @GetMapping("/shopPay")
    public List<ShopUserDTO> getShopUsersByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year) {
        return shopPayService.getShopUsersByMonthAndYear(month, year);
    }
    @PutMapping("/confirm/{shopPayId}")
    public ResponseEntity<ShopPayResponse> confirmPayForShop(@PathVariable int shopPayId) {
        ShopPayResponse response = shopPayService.confirmPayForShop(shopPayId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
