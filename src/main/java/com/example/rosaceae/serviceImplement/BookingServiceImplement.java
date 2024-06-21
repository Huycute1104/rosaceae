package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.BookingDTO;
import com.example.rosaceae.dto.Request.BookingRequest.ChangeBookingStatusRequest;
import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;
import com.example.rosaceae.enums.BookingStatus;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.Booking;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.TimeBooking;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.BookingRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.TimeBookingRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class BookingServiceImplement implements BookingService {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private TimeBookingRepo timeBookingRepo;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {
        // Step 1: Check if user and item exist
        Optional<User> user = userRepo.findById(request.getUsersId());
        Optional<Item> item = itemRepo.findById(request.getItemId());

        if (user.isEmpty()) {
            return BookingResponse.builder()
                    .status("User Not Found")
                    .build();
        }

        if (item.isEmpty()) {
            return BookingResponse.builder()
                    .status("Item Not Found")
                    .build();
        }

        // Step 2: Convert timestamp to Date object
        Date bookingDate = new Date(request.getDatetime());

        // Step 3: Check if bookingDate already exists in database
        boolean isBookingDateExists = bookingRepo.existsByBookingDate(bookingDate);
        if (isBookingDateExists) {
            return BookingResponse.builder()
                    .status("Booking for this date already exists")
                    .build();
        }

        // Step 4: Extract hour from bookingDate to determine timeID
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Step 5: Determine TimeID based on hour
        int timeID;
        switch (hour) {
            case 7: timeID = 1; break;
            case 8: timeID = 2; break;
            case 9: timeID = 3; break;
            case 10: timeID = 4; break;
            case 11: timeID = 5; break;
            case 12: timeID = 6; break;
            case 13: timeID = 7; break;
            case 14: timeID = 8; break;
            case 15: timeID = 9; break;
            case 16: timeID = 10; break;
            case 17: timeID = 11; break;
            case 18: timeID = 12; break;
            case 19: timeID = 13; break;
            case 20: timeID = 14; break;
            default:
                return BookingResponse.builder()
                        .status("Invalid booking hour")
                        .build();
        }

        // Step 6: Find TimeBooking based on timeID
        Optional<TimeBooking> timeBooking = timeBookingRepo.findById(timeID);
        if (timeBooking.isEmpty()) {
            return BookingResponse.builder()
                    .status("TimeBooking Not Found")
                    .build();
        }

        // Step 7: Extract date part from bookingDate
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date bookingDateOnly = calendar.getTime();

        // Step 8: Check if time slot is already booked for the specific day and timeID
        boolean isTimeSlotBooked = bookingRepo.existsByTimeBookingAndBookingDate(timeBooking.get(), bookingDateOnly);
        if (isTimeSlotBooked) {
            return BookingResponse.builder()
                    .status("Time slot already booked for the specified date and time")
                    .build();
        }

        // Step 9: Create and save Booking
        Booking booking = Booking.builder()
                .customer(user.get())
                .service(item.get())
                .timeBooking(timeBooking.get())
                .bookingDate(bookingDate)
                .status(BookingStatus.PENDING)
                .build();

        bookingRepo.save(booking);

        // Step 10: Return BookingResponse
        return BookingResponse.builder()
                .booking(booking)
                .status("Booking Created Successfully")
                .build();
    }






    @Override
    public Page<Booking> getBookings(Pageable pageable) {
        return bookingRepo.findAll(pageable);
    }
    @Override
    public Page<BookingDTO> getBookingsByUser(int userId, Pageable pageable) {
        return bookingRepo.findByService_User_UsersID(userId, pageable).map(booking -> {
            // Map Booking entity to BookingDTO
            return new BookingDTO(booking);
        });
    }

    @Override
    public String changeBookingStatus(ChangeBookingStatusRequest changeBookingStatusRequest) {
        Optional<Booking> optionalBooking = bookingRepo.findById(changeBookingStatusRequest.getBookingId());

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            try {
                BookingStatus status = BookingStatus.valueOf(changeBookingStatusRequest.getStatus().toUpperCase());
                booking.setStatus(status);
                bookingRepo.save(booking);
                return "Booking status updated successfully";
            } catch (IllegalArgumentException e) {
                return "Invalid status value";
            }
        } else {
            return "Booking not found";
        }
    }
    @Override
    public Page<Booking> getBookingsByUserId(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingRepo.findByCustomer_UsersID(userId, pageable);
    }

}
