package com.example.rosaceae.repository;

import com.example.rosaceae.model.Cart;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User userId);

    Optional<Cart> findByUserAndItem(User user, Item item);

    @Query("SELECT c FROM Cart c WHERE c.user.usersID = :customerId AND c.item.itemType.itemTypeId = :type")
    Page<Cart> findByUserIdAndItemTypeId(@Param("customerId") int customerId, @Param("type") int type, Pageable pageable);

}


