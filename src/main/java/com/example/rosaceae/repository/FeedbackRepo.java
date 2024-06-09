package com.example.rosaceae.repository;

import com.example.rosaceae.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findByItem_ItemId(int itemId, Pageable pageable);
}
