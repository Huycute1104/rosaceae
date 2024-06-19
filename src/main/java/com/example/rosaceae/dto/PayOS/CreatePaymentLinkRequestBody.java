package com.example.rosaceae.dto.PayOS;

import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {
  private String description;
  private String returnUrl;
  private String cancelUrl;
  private float total;
  private int voucherId;
  private int customerId;
  private String customerPhone;
  private String customerAddress;
  private String customerName;
  private List<CreateOrderRequest.OrderItemRequest> items;

  @Data
  public static class OrderItemRequest {
    private int itemId;
    private int quantity;
  }

}