package com.example.rosaceae.dto.Data;

import com.example.rosaceae.model.Booking;
import lombok.Data;

@Data
public class BookingDTO {
    private int bookingId;
    private String customerName;
    private String serviceName;
    private String bookingDate;
    private String time;
    private String status;

    public BookingDTO(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.customerName = booking.getCustomer().getAccountName();
        this.serviceName = booking.getService().getItemName();
        this.bookingDate = booking.getBookingDate().toString();
        this.time = booking.getTimeBooking().getTime();
        this.status = booking.getStatus().name();
    }
}
