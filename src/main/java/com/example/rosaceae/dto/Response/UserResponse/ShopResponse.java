package com.example.rosaceae.dto.Response.UserResponse;

import com.example.rosaceae.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponse {
    private String msg;
    private int status;
    private User userInfo;
}
