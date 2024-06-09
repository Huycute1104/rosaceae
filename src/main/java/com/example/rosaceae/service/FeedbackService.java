package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.dto.Request.FeedbackRequest.UpdateFeedbackRequest;
import com.example.rosaceae.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(CreateFeedbackRequest request);
    Feedback updateFeedback(UpdateFeedbackRequest request);
    Page<Feedback> getFeedbackByItemId(int itemId, Pageable pageable);
    Page<Feedback> getFeedbackByItemIdAndUserId(int itemId, int userId, Pageable pageable);
}