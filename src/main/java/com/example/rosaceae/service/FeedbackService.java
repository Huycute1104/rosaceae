package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.dto.Request.FeedbackRequest.UpdateFeedbackRequest;
import com.example.rosaceae.model.Feedback;

public interface FeedbackService {
    Feedback createFeedback(CreateFeedbackRequest request);
    Feedback updateFeedback(UpdateFeedbackRequest request);
}