package com.example.rosaceae.dto.PayOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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


}