package com.example.rosaceae.dto.Response.BookingResponse;

import com.example.rosaceae.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteBooking {
    private Booking booking;
    private int status;
    private String message;
}
