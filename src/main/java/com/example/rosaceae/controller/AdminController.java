package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.dto.Response.UserResponse.ShopPayResponse;
import com.example.rosaceae.service.ShopPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ShopUserDTO> getShopUsersByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return shopPayService.getShopUsersByMonthAndYear(month, year, pageable);
    }
    @PutMapping("/confirm/{shopPayId}")
    public ResponseEntity<ShopPayResponse> confirmPayForShop(@PathVariable int shopPayId) {
        ShopPayResponse response = shopPayService.confirmPayForShop(shopPayId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
