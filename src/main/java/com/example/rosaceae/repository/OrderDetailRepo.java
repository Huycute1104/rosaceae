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
    long countByItemUserUsersID(int userId);
    @Query("SELECT SUM(od.priceForShop) FROM OrderDetail od JOIN od.item i JOIN i.user u " +
            "WHERE u.usersID = :userId AND od.order.orderStatus = 'DELIVERED' " +
            "AND MONTH(od.order.orderDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(od.order.orderDate) = YEAR(CURRENT_DATE)")
    Float findTotalPriceForShopByUserIdAndCurrentMonthAndDelivered(@Param("userId") int userId);
}
