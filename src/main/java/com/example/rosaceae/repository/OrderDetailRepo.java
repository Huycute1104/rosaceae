package com.example.rosaceae.repository;

import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Integer> {
    @Query("SELECT od FROM OrderDetail od JOIN od.item i WHERE i.user.usersID = :userId")
    Page<OrderDetail> findByItemUserId(@Param("userId") int userId, Pageable pageable);
    long countByItemUserUsersID(int userId);
//    @Query("SELECT SUM(od.priceForShop) FROM OrderDetail od JOIN od.item i JOIN i.user u " +
//            "WHERE u.usersID = :userId AND od.order.orderStatus = 'DELIVERED' " +
//            "AND MONTH(od.order.orderDate) = MONTH(CURRENT_DATE) " +
//            "AND YEAR(od.order.orderDate) = YEAR(CURRENT_DATE)")
//    Float findTotalPriceForShopByUserIdAndCurrentMonthAndDelivered(@Param("userId") int userId);
    //sum cũ
//    @Query("SELECT SUM(od.priceForShop) FROM OrderDetail od JOIN od.item i JOIN i.user u " +
//        "WHERE u.usersID = :userId AND od.order.orderStatus = 'DELIVERED' " +
//        "AND MONTH(od.order.orderDate) = :month " +
//        "AND YEAR(od.order.orderDate) = :year")
//Float findTotalPriceForShopByUserIdAndMonthAndYearAndDelivered(
//        @Param("userId") int userId,
//        @Param("month") int month,
//        @Param("year") int year);
//kết thúc sum cũ

    //sum mới
@Query("SELECT SUM(od.priceForShop) FROM OrderDetail od JOIN od.item i JOIN i.user u " +
        "WHERE u.usersID = :userId AND (od.order.orderStatus = 'DELIVERED' OR od.order.orderStatus = 'BOOKING_COMPLETED') " +
        "AND MONTH(od.order.orderDate) = :month " +
        "AND YEAR(od.order.orderDate) = :year")
Float findTotalPriceForShopByUserIdAndMonthAndYearAndStatus(
        @Param("userId") int userId,
        @Param("month") int month,
        @Param("year") int year);

    @Query("SELECT SUM(od.price) FROM OrderDetail od " +
            "WHERE od.order.orderStatus = 'DELIVERED' " +
            "AND MONTH(od.order.orderDate) = :month " +
            "AND YEAR(od.order.orderDate) = :year")
    Float findTotalPriceForAdminByMonthAndYearAndDelivered(
            @Param("month") int month,
            @Param("year") int year);

//    @Query("SELECT DAY(o.orderDate), SUM(od.priceForShop) " +
//            "FROM Order o " +
//            "JOIN o.orderDetails od " +
//            "JOIN od.item i " +
//            "WHERE i.user.usersID = :userId AND MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year " +
//            "GROUP BY DAY(o.orderDate)")
//    List<Object[]> calculateTotalPriceForShopByDay(@Param("userId") int userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT DAY(o.orderDate), SUM(od.priceForShop) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.item i " +
            "WHERE i.user.usersID = :userId " +
            "AND MONTH(o.orderDate) = :month " +
            "AND YEAR(o.orderDate) = :year " +
            "AND o.orderStatus = 'DELIVERED' " +
            "GROUP BY DAY(o.orderDate)")
    List<Object[]> calculateTotalPriceForShopByDay(@Param("userId") int userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT DAY(o.orderDate), SUM(od.price) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "WHERE MONTH(o.orderDate) = :month " +
            "AND YEAR(o.orderDate) = :year " +
            "AND o.orderStatus = 'DELIVERED' " +
            "GROUP BY DAY(o.orderDate)")
    List<Object[]> calculateTotalPriceForAdminByDay(@Param("month") int month, @Param("year") int year);


}
