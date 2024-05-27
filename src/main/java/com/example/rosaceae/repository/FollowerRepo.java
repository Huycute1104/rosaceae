package com.example.rosaceae.repository;

import com.example.rosaceae.model.Follower;
import com.example.rosaceae.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepo extends JpaRepository<Follower, Integer> {
    Follower findByCustomerAndShop(User customer, User shop);
}
