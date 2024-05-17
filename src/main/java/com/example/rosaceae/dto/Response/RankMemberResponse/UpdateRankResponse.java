package com.example.rosaceae.dto.Response.RankMemberResponse;

import com.example.rosaceae.model.RankMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRankResponse {
    private RankMember rankMember;
    private String status;
}
