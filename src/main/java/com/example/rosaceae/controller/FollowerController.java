package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.FollowerRequest.CreateFollowerRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follower")
@RequiredArgsConstructor
public class FollowerController {
    @Autowired
    private final FollowerService followerService;

    @PostMapping("/toggle")
    public UserResponse toggleFollower(@RequestBody CreateFollowerRequest createFollowerRequest) {
        return followerService.toggleFollower(createFollowerRequest);
    }
}
