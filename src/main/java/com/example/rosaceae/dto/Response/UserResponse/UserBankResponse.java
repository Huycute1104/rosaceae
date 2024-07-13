package com.example.rosaceae.dto.Response.UserResponse;

import com.example.rosaceae.model.UserBank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBankResponse {
    private UserBank userBank;
    private String message;
    private int status;
}
