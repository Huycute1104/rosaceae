package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.dto.Request.FeedbackRequest.UpdateFeedbackRequest;
import com.example.rosaceae.model.Feedback;
import com.example.rosaceae.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("")
    public ResponseEntity<Feedback> createFeedback(@RequestBody CreateFeedbackRequest request) {
        Feedback feedback = feedbackService.createFeedback(request);
        return ResponseEntity.ok(feedback);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable int id, @RequestBody UpdateFeedbackRequest request) {
        request.setFeedbackId(id);
        Feedback feedback = feedbackService.updateFeedback(request);
        return ResponseEntity.ok(feedback);
    }
    @GetMapping("/item/{itemId}")
    public ResponseEntity<Page<Feedback>> getFeedbackByItemId(
            @PathVariable int itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> feedbackPage = feedbackService.getFeedbackByItemId(itemId, pageable);
        return ResponseEntity.ok(feedbackPage);
    }
    @GetMapping("/item/{itemId}/user/{userId}")
    public ResponseEntity<Page<Feedback>> getFeedbackByItemIdAndUserId(
            @PathVariable int itemId,
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> feedbackPage = feedbackService.getFeedbackByItemIdAndUserId(itemId, userId, pageable);
        return ResponseEntity.ok(feedbackPage);
    }

}