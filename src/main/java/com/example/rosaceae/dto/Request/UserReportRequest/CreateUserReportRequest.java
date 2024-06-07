package com.example.rosaceae.dto.Request.UserReportRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserReportRequest {
    private Integer usersId;
    private Integer shopId;
    private int itemId;
    private Integer reportId;
    private String description;
}
