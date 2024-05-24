package com.example.rosaceae.repository;

import com.example.rosaceae.model.ItemType;
import com.example.rosaceae.model.RankMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemTypeRepo extends JpaRepository<ItemType, Integer> {
    Optional<ItemType> findByItemTypeName(String name);
    Optional<ItemType> findByItemTypeId(int itemTypeId);
}
