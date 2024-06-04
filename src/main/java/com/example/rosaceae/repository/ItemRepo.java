package com.example.rosaceae.repository;

import com.example.rosaceae.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByItemId(int id);
    List<Item> findByUserUsersID(int id);
    Page<Item> findByUserUsersID(int userId, Pageable pageable);

}
