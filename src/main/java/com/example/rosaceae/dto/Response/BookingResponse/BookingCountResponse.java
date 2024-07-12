package com.example.rosaceae.dto.Response.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingCountResponse {
    private int day;
    private long count;
}
