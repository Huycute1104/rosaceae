package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Request.RankMemberRequest.UpdateRankRequest;
import com.example.rosaceae.dto.Response.RankMemberResponse.UpdateRankResponse;
import com.example.rosaceae.dto.Response.UserResponse.CreateRankResponse;
import com.example.rosaceae.model.RankMember;

import java.util.List;
import java.util.Optional;

public interface RankMemberService {
    public CreateRankResponse createRank(CreateRankRequet createRankRequet);
    public UpdateRankResponse updateRank(int id,UpdateRankRequest updateRankRequest);
    List<RankMember> getAllRank();
    Optional<RankMember> getRankById(int id);
}