package com.example.rosaceae.repository;

import com.example.rosaceae.model.Booking;
import com.example.rosaceae.model.TimeBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    Page<Booking> findAll(Pageable pageable);
    Page<Booking> findByService_User_UsersID(int userId, Pageable pageable);
    Page<Booking> findByCustomer_UsersID(int userId, Pageable pageable);
    boolean existsByTimeBookingAndBookingDate(TimeBooking timeBooking, Date bookingDate);
    boolean existsByBookingDate(Date bookingDate);
    List<Booking> findByService_ItemIdIn(List<Integer> itemIds);
    @Query("SELECT DAY(b.bookingDate) as day, COUNT(b) as count " +
            "FROM Booking b " +
            "WHERE b.status = com.example.rosaceae.enums.BookingStatus.COMPLETED " +
            "AND MONTH(b.bookingDate) = :month " +
            "AND YEAR(b.bookingDate) = :year " +
            "GROUP BY DAY(b.bookingDate)")
    List<Object[]> countCompletedBookingsByDay(@Param("month") int month, @Param("year") int year);
}
