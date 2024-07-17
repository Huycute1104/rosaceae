package com.example.rosaceae.dto.Data;

import com.example.rosaceae.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

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
    private String CustomerAddress;
    private String CustomerPhone;
    private int itemTypeId;
    private OrderStatus status;
    private String itemImages;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date orderDate;
}
