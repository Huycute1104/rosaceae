package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.FollowerRequest.CreateFollowerRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Follower;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.FollowerRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerServiceImplement implements FollowerService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerRepo followerRepo;
    @Override
    public UserResponse toggleFollower(CreateFollowerRequest createFollowerRequest) {
        // Validate that CustomerID is not the same as ShopID
        if (createFollowerRequest.getCustomerId() == createFollowerRequest.getShopId()) {
            return UserResponse.builder().status("Thất bại: Không thể follow chính mình!").build();
        }

        // Validate users
        User customer = userRepo.findById(createFollowerRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        User shop = userRepo.findById(createFollowerRequest.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        // Check if the follower relationship exists
        Follower follower = followerRepo.findByCustomerAndShop(customer, shop);
        if (follower != null) {
            // Unfollow the shop
            followerRepo.delete(follower);
            return UserResponse.builder().status("Unfollowed successfully").build();
        } else {
            // Follow the shop
            follower = Follower.builder()
                    .customer(customer)
                    .shop(shop)
                    .build();
            followerRepo.save(follower);
            return UserResponse.builder().status("Followed successfully").build();
        }
    }
}
