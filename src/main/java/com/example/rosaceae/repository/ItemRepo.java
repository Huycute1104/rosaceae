package com.example.rosaceae.repository;

import com.example.rosaceae.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    Optional<Item> findByItemId(int id);
}
