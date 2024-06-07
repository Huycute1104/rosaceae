package com.example.rosaceae.dto.Data;

import com.example.rosaceae.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
//    private User user;
    private int usersID;
    private String accountName;
    private String email;
    private String phone;
    private String address;
    private float rate;
    private double userWallet;
    private boolean userStatus;
    private String role;
    private String coverImages;
    Page<ItemDTO> items;
}
