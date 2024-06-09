package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.dto.Request.FeedbackRequest.UpdateFeedbackRequest;
import com.example.rosaceae.model.Feedback;
import com.example.rosaceae.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}