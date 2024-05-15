package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Request.RankMemberRequest.UpdateRankRequest;
import com.example.rosaceae.dto.Response.RankMemberResponse.UpdateRankResponse;
import com.example.rosaceae.dto.Response.UserResponse.CreateRankResponse;
import com.example.rosaceae.model.RankMember;
import com.example.rosaceae.service.RankMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rankmember")
@RequiredArgsConstructor
public class RankMemberController {

    @Autowired
    private RankMemberService memberService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<RankMember> getAllUsers() {
        return memberService.getAllRank();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<RankMember> getCategoryByID(@PathVariable int id) {
        return memberService.getRankById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<CreateRankResponse> createCategory(@RequestBody CreateRankRequet request) {
        return ResponseEntity.ok(memberService.createRank(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<UpdateRankResponse> updateUser(
            @PathVariable int id,
            @RequestBody UpdateRankRequest request) {
        return ResponseEntity.ok(memberService.updateRank(id, request));
    }
}
