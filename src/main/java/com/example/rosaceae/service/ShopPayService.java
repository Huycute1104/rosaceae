package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.dto.Response.UserResponse.ShopPayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopPayService {
    public Page<ShopUserDTO> getShopUsersByMonthAndYear(int month, int year, Pageable pageable);
    ShopPayResponse confirmPayForShop(int shopPayId);
}
