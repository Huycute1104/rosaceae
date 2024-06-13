package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
}
