package com.example.rosaceae.dto.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopLocation {
    private int locationID;
    private int usersID;
    private String accountName;
    private String locationUrl;
}
