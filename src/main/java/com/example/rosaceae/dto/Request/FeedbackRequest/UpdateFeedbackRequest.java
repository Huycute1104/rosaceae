package com.example.rosaceae.dto.Request.FeedbackRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFeedbackRequest {
    private int feedbackId;
    private String feedback;
    private int rateStar;
}