package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.UserReportRequest.CreateUserReportRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userReport")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUserReport (@RequestBody CreateUserReportRequest userReportRequest) {
        UserResponse response = userReportService.createUserReport(userReportRequest);
        if ("Report created successfully".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
