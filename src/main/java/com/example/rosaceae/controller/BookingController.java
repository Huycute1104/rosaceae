package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.BookingDTO;
import com.example.rosaceae.dto.Request.BookingRequest.ChangeBookingStatusRequest;
import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;
import com.example.rosaceae.model.Booking;
import com.example.rosaceae.model.TimeBooking;
import com.example.rosaceae.service.BookingService;
import com.example.rosaceae.service.TimeBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TimeBookingService timeBookingService;

    @PostMapping("")
    public BookingResponse createBooking(@RequestBody CreateBookingRequest request) {
        return bookingService.createBooking(request);
    }
    @GetMapping
    public Page<Booking> getBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getBookings(pageable);
    }
    @GetMapping("/user/{userId}")
    public Page<BookingDTO> getBookingsByUser(@PathVariable int userId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getBookingsByUser(userId, pageable);
    }
    @PutMapping("/status")
    public String changeBookingStatus(@RequestBody ChangeBookingStatusRequest changeBookingStatusRequest) {
        return bookingService.changeBookingStatus(changeBookingStatusRequest);
    }
    @GetMapping("/customer/{userId}")
    public ResponseEntity<Page<BookingDTO>> getBookingsByUserId(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Booking> bookings = bookingService.getBookingsByUserId(userId, page, size);
        Page<BookingDTO> bookingDTOs = bookings.map(BookingDTO::new);
        return ResponseEntity.ok(bookingDTOs);
    }
    @GetMapping("/time-booking")
    public ResponseEntity<List<TimeBooking>> getTimeBooking() {
        return ResponseEntity.ok(timeBookingService.getAllBookingTime());
    }
}