package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.dto.Response.UserResponse.ShopPayResponse;

import java.util.List;

public interface ShopPayService {
    public List<ShopUserDTO> getShopUsersByMonthAndYear(int month, int year);
    ShopPayResponse confirmPayForShop(int shopPayId);
}
