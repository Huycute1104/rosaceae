package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;
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
        Optional<User> user = userRepo.findById(request.getUsersId());
        Optional<Item> item = itemRepo.findById(request.getItemId());
        Optional<TimeBooking> timeBooking = timeBookingRepo.findById(request.getTimeId());

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

        // Kiểm tra xem itemTypeId có bằng 1 không
        if (item.get().getItemType().getItemTypeId() != 1) {
            return BookingResponse.builder()
                    .status("Item is not of required type (itemTypeId != 1)")
                    .build();
        }

        if (timeBooking.isEmpty()) {
            return BookingResponse.builder()
                    .status("TimeBooking Not Found")
                    .build();
        }

        // Kiểm tra xem đã có khách hàng nào đặt vào thời gian timeID này trong ngày cụ thể chưa
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfDay = cal.getTime();

        boolean isTimeSlotBooked = bookingRepo.existsByTimeBookingAndBookingDateBetween(timeBooking.get(), startOfDay, endOfDay);

        if (isTimeSlotBooked) {
            return BookingResponse.builder()
                    .status("Time slot already booked for today")
                    .build();
        }

        Booking booking = Booking.builder()
                .customer(user.get())
                .service(item.get())
                .timeBooking(timeBooking.get())
                .bookingDate(today)
                .status(OrderStatus.PENDING)
                .build();

        bookingRepo.save(booking);

        return BookingResponse.builder()
                .booking(booking)
                .status("Booking Created Successfully")
                .build();
    }
    @Override
    public Page<Booking> getBookings(Pageable pageable) {
        return bookingRepo.findAll(pageable);
    }
}
