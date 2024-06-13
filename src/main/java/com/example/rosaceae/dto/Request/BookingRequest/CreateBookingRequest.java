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
    private int usersId;
    private int itemId;
    private int timeId;
}
