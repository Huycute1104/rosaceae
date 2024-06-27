package com.example.rosaceae.dto.Data;

import com.example.rosaceae.enums.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private int orderDetailId;
    private int quantity;
    private float price;
    private int itemId;
    private String itemName;
    private String CustomerName;
    private int itemTypeId;
    private OrderStatus status;
}
