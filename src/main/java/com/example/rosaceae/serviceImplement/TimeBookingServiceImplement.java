package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.TimeBookingRequest.TimeBookingRequest;
import com.example.rosaceae.dto.Response.TimeBookingResponse.TimeBookingResponse;
import com.example.rosaceae.model.TimeBooking;
import com.example.rosaceae.repository.TimeBookingRepo;
import com.example.rosaceae.service.TimeBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeBookingServiceImplement implements TimeBookingService {
    @Autowired
    private TimeBookingRepo timeBookingRepo;
    @Override
    public TimeBookingResponse createBookingTime(TimeBookingRequest request) {
        TimeBooking timeBooking = TimeBooking.builder()
                .time(request.getTime())
                .build();
        timeBookingRepo.save(timeBooking);
        return TimeBookingResponse.builder()
                .status("Create successful")
                .timeBooking(timeBooking)
                .build();

    }
}
