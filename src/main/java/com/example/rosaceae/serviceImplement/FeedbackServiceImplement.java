package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.FeedbackRequest.CreateFeedbackRequest;
import com.example.rosaceae.model.Feedback;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.FeedbackRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
