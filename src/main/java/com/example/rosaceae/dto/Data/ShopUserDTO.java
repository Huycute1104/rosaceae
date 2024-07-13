package com.example.rosaceae.dto.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserDTO {
    private int userId;
    private String email;
    private String accountName;
    private String bankName;
    private String bankAccountNumber;
    private float money;
    private boolean status;
}
