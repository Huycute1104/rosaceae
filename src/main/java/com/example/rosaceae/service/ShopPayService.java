package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.ShopUserDTO;

import java.util.List;

public interface ShopPayService {
    public List<ShopUserDTO> getShopUsersByMonthAndYear(int month, int year);
}
