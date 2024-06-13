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
public class BookingResponse {
    private Booking booking;
    private String status;
}
