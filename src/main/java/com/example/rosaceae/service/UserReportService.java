package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.UserReportRequest.CreateUserReportRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;

public interface UserReportService {
    UserResponse createUserReport (CreateUserReportRequest userReportRequest);
}
