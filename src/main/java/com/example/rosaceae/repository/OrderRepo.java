package com.example.rosaceae.repository;

import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.Order;
import com.example.rosaceae.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    Optional<Order> findByOrderId(int id);
    Page<Order> findByCustomerUsersID(int id, Pageable pageable);
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.item i WHERE i.user.usersID = :userId")
    Page<Order> findByItemUserId(@Param("userId") int userId, Pageable pageable);
    long countByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.orderDetails od JOIN od.item i WHERE o.orderStatus = :orderStatus AND i.user.usersID = :shopOwnerId")
    long countByOrderStatusAndShopOwnerId(OrderStatus orderStatus, int shopOwnerId);
}
