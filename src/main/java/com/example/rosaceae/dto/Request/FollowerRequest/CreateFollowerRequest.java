package com.example.rosaceae.dto.Request.FollowerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFollowerRequest {
    private int customerId;
    private int shopId;
}
