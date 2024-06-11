package com.example.rosaceae.repository;

import com.example.rosaceae.model.TimeBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeBookingRepo extends JpaRepository<TimeBooking,Integer> {
}
