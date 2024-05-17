package com.example.rosaceae.dto.Response.UserResponse;

import com.example.rosaceae.model.RankMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRankResponse {
    private String status;
    private RankMember rankMember;
}
