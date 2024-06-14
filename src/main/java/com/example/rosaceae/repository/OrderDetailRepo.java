package com.example.rosaceae.repository;

import com.example.rosaceae.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Integer> {
    @Query("SELECT od FROM OrderDetail od JOIN od.item i WHERE i.user.usersID = :userId")
    Page<OrderDetail> findByItemUserId(@Param("userId") int userId, Pageable pageable);
}
