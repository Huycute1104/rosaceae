package com.example.rosaceae.dto.Response.TimeBookingResponse;

import com.example.rosaceae.model.TimeBooking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeBookingResponse {
    private TimeBooking timeBooking;
    private String status;
}
