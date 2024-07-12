package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.BookingDTO;
import com.example.rosaceae.dto.Request.BookingRequest.ChangeBookingStatusRequest;
import com.example.rosaceae.dto.Request.BookingRequest.CreateBookingRequest;
import com.example.rosaceae.dto.Response.BookingResponse.BookingCountResponse;
import com.example.rosaceae.dto.Response.BookingResponse.BookingResponse;
import com.example.rosaceae.dto.Response.BookingResponse.CompleteBooking;
import com.example.rosaceae.enums.BookingStatus;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.*;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;

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

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {
        // Step 1: Check if user and item exist
        Optional<User> user = userRepo.findByEmail(request.getEmail());
        Optional<Item> item = itemRepo.findById(request.getItemId());
        System.out.println(request.getEmail());

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

//        // Step 4: Extract hour from bookingDate to determine timeID
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(bookingDate);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//
//        // Step 5: Determine TimeID based on hour
//        int timeID;
//        switch (hour) {
//            case 7: timeID = 1; break;
//            case 8: timeID = 2; break;
//            case 9: timeID = 3; break;
//            case 10: timeID = 4; break;
//            case 11: timeID = 5; break;
//            case 12: timeID = 6; break;
//            case 13: timeID = 7; break;
//            case 14: timeID = 8; break;
//            case 15: timeID = 9; break;
//            case 16: timeID = 10; break;
//            case 17: timeID = 11; break;
//            case 18: timeID = 12; break;
//            case 19: timeID = 13; break;
//            case 20: timeID = 14; break;
//            default:
//                return BookingResponse.builder()
//                        .status("Invalid booking hour")
//                        .build();
//        }

        // Step 6: Find TimeBooking based on timeID
        Optional<TimeBooking> timeBooking = timeBookingRepo.findById(request.getTimeBookingId());
        if (timeBooking.isEmpty()) {
            return BookingResponse.builder()
                    .status("TimeBooking Not Found")
                    .build();
        }

        // Step 7: Extract date part from bookingDate
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
        Date bookingDateOnly = new Date(request.getDatetime());

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
    public CompleteBooking completeBooking(int bookingId) {
        var booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return CompleteBooking.builder()
                    .booking(null)
                    .status(404)
                    .message("Booking not found")
                    .build();
        }
        if (!booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            return CompleteBooking.builder()
                    .booking(null)
                    .status(400)
                    .message("Booking not confirmed yet or is cancel")
                    .build();
        }
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepo.save(booking);

        // create order
        Order order = Order.builder()
                .customerPhone(booking.getCustomer().getPhone())
                .customerName(booking.getCustomer().getAccountName())
                .customerAddress(booking.getCustomer().getAddress())
                .orderDate(new Date())
                .total(booking.getService().getItemPrice())
                .orderStatus(OrderStatus.BOOKING_COMPLETED)
                .orderDetails(null)
                .orderCode(0)
                .customer(booking.getCustomer())
                .build();
        Set<OrderDetail> orderDetails = new HashSet<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(1)
                .price(booking.getService().getItemPrice())
                .priceForShop(booking.getService().getItemPrice())
                .item(booking.getService())
                .order(order)
                .build();
        orderDetails.add(orderDetail);
        order.setOrderDetails(orderDetails);

        orderRepo.save(order);
        return CompleteBooking.builder()
                .booking(booking)
                .status(200)
                .message("Booking completed")
                .build();
    }


    @Override
    public Page<Booking> getBookingsByUserId(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingRepo.findByCustomer_UsersID(userId, pageable);
    }

    @Override
    public Map<String, Double> getBookingStatusPercentages(int usersID) {
        List<Item> items = itemRepo.findByUser_UsersID(usersID);
        List<Integer> itemIds = items.stream().map(Item::getItemId).toList();

        List<Booking> bookings = bookingRepo.findByService_ItemIdIn(itemIds);
        int totalBookings = bookings.size();

        Map<String, Double> statusPercentages = new HashMap<>();
        if (totalBookings == 0) {
            for (BookingStatus status : BookingStatus.values()) {
                statusPercentages.put(status.name(), 0.0);
            }
            return statusPercentages;
        }

        for (BookingStatus status : BookingStatus.values()) {
            long count = bookings.stream().filter(booking -> booking.getStatus() == status).count();
            statusPercentages.put(status.name(), (count * 100.0) / totalBookings);
        }

        return statusPercentages;
    }
    @Override
    public Map<String, Double> getAllBookingStatusPercentages() {
        List<Booking> bookings = bookingRepo.findAll();
        int totalBookings = bookings.size();

        Map<String, Double> statusPercentages = new HashMap<>();
        if (totalBookings == 0) {
            for (BookingStatus status : BookingStatus.values()) {
                statusPercentages.put(status.name(), 0.0);
            }
            return statusPercentages;
        }

        for (BookingStatus status : BookingStatus.values()) {
            long count = bookings.stream().filter(booking -> booking.getStatus() == status).count();
            statusPercentages.put(status.name(), (count * 100.0) / totalBookings);
        }

        return statusPercentages;
    }

    @Override
    public List<BookingCountResponse> getCompletedBookingCountByDay(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<BookingCountResponse> dailyBookingCounts = new ArrayList<>();

        // Initialize the list with all days of the month set to zero count
        for (int day = 1; day <= daysInMonth; day++) {
            dailyBookingCounts.add(new BookingCountResponse(day, 0));
        }

        // Get the actual booking counts from the repository
        List<Object[]> results = bookingRepo.countCompletedBookingsByDay(month, year);

        // Update the count for each day based on the query results
        for (Object[] result : results) {
            int day = (int) result[0];
            long count = (long) result[1];
            dailyBookingCounts.set(day - 1, new BookingCountResponse(day, count));
        }

        return dailyBookingCounts;
    }


}
