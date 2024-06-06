package com.example.rosaceae.dto.Data;

import com.example.rosaceae.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private User user;
    List<ItemDTO> items;
}
