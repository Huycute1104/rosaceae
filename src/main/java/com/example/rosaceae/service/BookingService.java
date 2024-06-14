package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.BookingDTO;
import com.example.rosaceae.dto.Request.BookingRequest.ChangeBookingStatusRequest;
import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;
import com.example.rosaceae.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
    Page<Booking> getBookings(Pageable pageable);
    Page<BookingDTO> getBookingsByUser(int userId, Pageable pageable);
    String changeBookingStatus(ChangeBookingStatusRequest changeBookingStatusRequest);
}
