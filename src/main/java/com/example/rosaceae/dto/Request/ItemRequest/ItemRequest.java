package com.example.rosaceae.dto.Request.ItemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private String itemName;
    private int quantity;
    private Float price;
    private String description;
    private Float rate = 0.0f;
    private int commentCount = 0;
    private int countUsage = 0;
    private int discount;
    private  int shopId;
    private int itemTypeId;
    private int categoryId;
}
