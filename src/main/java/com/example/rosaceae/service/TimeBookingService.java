package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.TimeBookingRequest.TimeBookingRequest;
import com.example.rosaceae.dto.Response.TimeBookingResponse.TimeBookingResponse;
import com.example.rosaceae.model.TimeBooking;

import java.util.List;

public interface TimeBookingService {
    public TimeBookingResponse createBookingTime(TimeBookingRequest request);

    public List<TimeBooking> getAllBookingTime();
}
