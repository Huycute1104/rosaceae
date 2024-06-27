package com.example.rosaceae.dto.Request.BookingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private String email;
    private int itemId;
    private int timeBookingId;
    private long datetime;
}
