package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.FollowerRequest.CreateFollowerRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;

public interface FollowerService {
    UserResponse toggleFollower(CreateFollowerRequest createFollowerRequest);
}
