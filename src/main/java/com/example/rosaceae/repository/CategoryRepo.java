package com.example.rosaceae.repository;

import com.example.rosaceae.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Optional<Category> findCategoriesByCategoryName(String name);
    Optional<Category> findCategoriesByCategoryId(int id);
}
