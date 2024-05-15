package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.CreateRankRequet;
import com.example.rosaceae.dto.CreateRankResponse;
import com.example.rosaceae.model.RankMember;
import com.example.rosaceae.repository.RankMemberRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.RankMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankMemberServiceImplement implements RankMemberService {
    @Autowired
    private  RankMemberRepo memberRepo;

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
                        .rankMember(rank)
                        .build();
            }else{
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
    private boolean isValidName(String name) {
        //Check validate rank name
        return name != null && name.length() <= 20 && name.length() >= 3 && !name.matches(".*[^a-zA-Z0-9].*");
    }
}
