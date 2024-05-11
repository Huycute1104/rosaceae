package com.example.rosaceae.repository;

import com.example.rosaceae.model.RankMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankMemberRepo extends JpaRepository<RankMember,Integer> {
Optional<RankMember> findByRankName(String name);
}
