package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Request.RankMemberRequest.UpdateRankRequest;
import com.example.rosaceae.dto.Response.RankMemberResponse.UpdateRankResponse;
import com.example.rosaceae.dto.Response.UserResponse.CreateRankResponse;
import com.example.rosaceae.model.RankMember;
import com.example.rosaceae.repository.RankMemberRepo;
import com.example.rosaceae.service.RankMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankMemberServiceImplement implements RankMemberService {
    @Autowired
    private RankMemberRepo memberRepo;

    @Override
    public CreateRankResponse createRank(CreateRankRequet createRankRequet) {
        String name = createRankRequet.getRankName();
        //check exist
        var rank = memberRepo.findByRankName(name).orElse(null);
        if (rank == null) {
            if (isValidName(name)) {
                RankMember cate = RankMember.builder()
                        .rankName(name)
                        .build();
                memberRepo.save(cate);
                return CreateRankResponse.builder()
                        .status("Create new rank successfully")
                        .rankMember(cate)
                        .build();
            } else {
                return CreateRankResponse.builder()
                        .status("The RankName must be between 3 and 20 char and should not contain any special characters.")
                        .rankMember(null)
                        .build();
            }

        } else {
            return CreateRankResponse.builder()
                    .status("Rank is exist")
                    .rankMember(null)
                    .build();
        }
    }

    @Override
    public UpdateRankResponse updateRank(int id, UpdateRankRequest updateRankRequest) {
        String rankName = updateRankRequest.getName();
        var rank = memberRepo.findByRankMemberID(id).orElse(null);
        if (rank != null) {
            if (isValidName(rankName)) {
                rank.setRankName(rankName);
                memberRepo.save(rank);
                return UpdateRankResponse.builder()
                        .status("Update Rank member successful")
                        .rankMember(rank)
                        .build();
            } else {
                return UpdateRankResponse.builder()
                        .rankMember(null)
                        .status("The RankName must be between 3 and 20 char and should not contain any special characters.")
                        .build();
            }
        } else {
            return UpdateRankResponse.builder()
                    .rankMember(null)
                    .status("RankMember not found")
                    .build();
        }
    }

    @Override
    public List<RankMember> getAllRank() {
        return memberRepo.findAll();
    }

    private boolean isValidName(String name) {
        //Check validate rank name
        return name != null && name.length() <= 20 && name.length() >= 3 && !name.matches(".*[^a-zA-Z0-9].*");
    }
}
