package com.example.rosaceae.repository;

import com.example.rosaceae.model.Cart;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User userId);

    Optional<Cart> findByUserAndItem(User user, Item item);
}
