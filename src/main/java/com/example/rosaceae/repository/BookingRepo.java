package com.example.rosaceae.repository;

import com.example.rosaceae.model.Booking;
import com.example.rosaceae.model.TimeBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    boolean existsByTimeBookingAndBookingDateBetween(TimeBooking timeBooking, Date startOfDay, Date endOfDay);
    Page<Booking> findAll(Pageable pageable);
    Page<Booking> findByService_User_UsersID(int userId, Pageable pageable);
}
