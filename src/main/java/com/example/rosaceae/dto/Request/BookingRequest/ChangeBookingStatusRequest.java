package com.example.rosaceae.dto.Request.BookingRequest;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeBookingStatusRequest {
    private int bookingId;
    private String status;
}
