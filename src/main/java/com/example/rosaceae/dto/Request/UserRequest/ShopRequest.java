package com.example.rosaceae.dto.Request.UserRequest;

import com.example.rosaceae.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
}
