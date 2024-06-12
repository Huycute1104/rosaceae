package com.example.rosaceae.dto.Data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private int orderDetailId;
    private int quantity;
    private float price;
    private float priceForShop;
    private int itemId;
    private String itemName;
}
