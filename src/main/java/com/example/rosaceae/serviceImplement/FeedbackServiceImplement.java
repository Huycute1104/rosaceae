package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.dto.Request.FeedbackRequest.UpdateFeedbackRequest;
import com.example.rosaceae.model.Feedback;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.FeedbackRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImplement implements FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Feedback createFeedback(CreateFeedbackRequest request) {
        User user = userRepo.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepo.findById(request.getItemId()).orElseThrow(() -> new RuntimeException("Item not found"));

        Feedback feedback = Feedback.builder()
                .feedback(request.getFeedback())
                .rateStar(request.getRateStar())
                .feedBackAt(new Date())
                .user(user)
                .item(item)
                .build();

        return feedbackRepo.save(feedback);
    }
    @Override
    public Feedback updateFeedback(UpdateFeedbackRequest request) {
        Feedback feedback = feedbackRepo.findById(request.getFeedbackId()).orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setFeedback(request.getFeedback());
        feedback.setRateStar(request.getRateStar());
        feedback.setFeedBackAt(new Date());
        return feedbackRepo.save(feedback);
    }
    @Override
    public Page<Feedback> getFeedbackByItemId(int itemId, Pageable pageable) {
        return feedbackRepo.findByItem_ItemId(itemId, pageable);
    }
    @Override
    public Page<Feedback> getFeedbackByItemIdAndUserId(int itemId, int userId, Pageable pageable) {
        return feedbackRepo.findByItem_ItemIdAndUser_UsersID(itemId, userId, pageable);
    }
}
