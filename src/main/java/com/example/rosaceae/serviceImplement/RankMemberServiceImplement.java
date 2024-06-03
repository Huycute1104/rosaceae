package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Request.RankMemberRequest.UpdateRankRequest;
import com.example.rosaceae.dto.Response.RankMemberResponse.RankResponse;
import com.example.rosaceae.dto.Response.RankMemberResponse.UpdateRankResponse;
import com.example.rosaceae.dto.Response.UserResponse.CreateRankResponse;
import com.example.rosaceae.model.RankMember;
import com.example.rosaceae.repository.RankMemberRepo;
import com.example.rosaceae.service.RankMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                        .rankPoint(createRankRequet.getRankPoint())
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
            var name = memberRepo.findByRankName(rankName).orElse(null);
            if(name != null){
                return UpdateRankResponse.builder()
                        .rankMember(null)
                        .status("Rank name is exist")
                        .build();
            }
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

    @Override
    public Page<RankMember> getAllRank(Pageable pageable) {
        return memberRepo.findAll(pageable);
    }

    @Override
    public Optional<RankMember> getRankById(int id) {
        return memberRepo.findByRankMemberID(id);
    }

    @Override
    public RankResponse deleteRank(int id) {
        var rank = memberRepo.findByRankMemberID(id).orElse(null);
        if (rank == null) {
            return RankResponse.builder()
                    .status("Rank not found")
                    .build();
        }else{
            memberRepo.delete(rank);
            return RankResponse.builder()
                    .status("Rank deleted successfully")
                    .build();
        }
    }

    private boolean isValidName(String name) {
        //Check validate rank name
        return name != null && name.length() <= 20 && name.length() >= 3;
    }
}
