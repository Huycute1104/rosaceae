package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.TimeBookingRequest.TimeBookingRequest;
import com.example.rosaceae.dto.Response.TimeBookingResponse.TimeBookingResponse;
import com.example.rosaceae.model.TimeBooking;

public interface TimeBookingService {
    public TimeBookingResponse createBookingTime(TimeBookingRequest request);
}
