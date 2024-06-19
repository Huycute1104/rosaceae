package com.example.rosaceae.dto.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private int orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date orderDate;
    private float total;
    private String orderStatus;
    private Set<OrderDetailDTO> orderDetails;
    private String customerPhone;
    private String customerAddress;
    private String customerName;
}
