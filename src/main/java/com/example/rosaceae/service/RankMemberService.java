package com.example.rosaceae.service;

import com.example.rosaceae.dto.CreateRankRequet;
import com.example.rosaceae.dto.CreateRankResponse;

public interface RankMemberService {
    public CreateRankResponse createRank(CreateRankRequet createRankRequet);
}