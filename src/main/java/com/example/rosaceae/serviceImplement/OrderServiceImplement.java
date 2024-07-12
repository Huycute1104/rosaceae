package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.OrderDTO;
import com.example.rosaceae.dto.Data.OrderDetailDTO;
import com.example.rosaceae.dto.Data.OrderMapper;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Request.OrderRequest.UpdateStatus;
import com.example.rosaceae.dto.Response.OrderResponse.*;
import com.example.rosaceae.enums.Fee;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.*;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderServiceImplement implements OrderService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CartRepo cartRepository;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public OrderResponse CreateOrder(CreateOrderRequest request) {
        var user = userRepo.findUserByUsersID(request.getCustomerId()).orElse(null);
        if (user == null) {
            return OrderResponse.builder()
                    .status("User Not Found")
                    .order(null)
                    .build();
        }
        float total = request.getTotal();
        if (total <= 0) {
            return OrderResponse.builder()
                    .status("Total must be a positive value.")
                    .order(null)
                    .build();
        }
        var voucher = voucherRepo.findVoucherByVoucherId(request.getVoucherId()).orElse(null);
        if(voucher != null){
            total = total - ((total * voucher.getValue())/100);
        }

        Order order = Order.builder()
                .orderDate(new Date())
                .total(total)
                .orderStatus(OrderStatus.PENDING)
                .customer(user)
                .voucher(voucher)
                .build();
        orderRepo.save(order);

        return OrderResponse.builder()
                .status("Order Created Successfully")
                .order(order)
                .build();

    }
    @Transactional
    @Override
    public OrderResponse createOrderWithDetails(CreateOrderRequest request) {
        var user = userRepo.findUserByUsersID(request.getCustomerId()).orElse(null);
        if (user == null) {
            return OrderResponse.builder()
                    .status("User Not Found")
                    .order(null)
                    .orderDetails(null)
                    .build();
        }

        // Check if items are provided in the request
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return OrderResponse.builder()
                    .status("No items provided in the request")
                    .order(null)
                    .orderDetails(null)
                    .build();
        }

        float calculatedTotal = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            var item = itemRepo.findById(itemRequest.getItemId()).orElse(null);
            if (item == null) {
                return OrderResponse.builder()
                        .status("Item with ID " + itemRequest.getItemId() + " not found")
                        .order(null)
                        .orderDetails(null)
                        .build();
            }

            if (item.getQuantity() < itemRequest.getQuantity()) {
                return OrderResponse.builder()
                        .status("Insufficient quantity for item with ID " + itemRequest.getItemId())
                        .order(null)
                        .orderDetails(null)
                        .build();
            }
            float discount = (float) item.getDiscount() /100;
            float itemDiscount = item.getItemPrice() * discount;
            float itemTotal = (item.getItemPrice()-itemDiscount) * itemRequest.getQuantity();
            calculatedTotal += itemTotal;

            OrderDetail orderDetail = OrderDetail.builder()
                    .item(item)
                    .quantity(itemRequest.getQuantity())
                    .price(itemTotal)
                    .priceForShop(itemTotal - (itemTotal * Fee.SHOP_FEE.getFee() / 100))
                    .build();
            orderDetails.add(orderDetail);
        }

        var voucher = voucherRepo.findVoucherByVoucherId(request.getVoucherId()).orElse(null);
        if (voucher != null) {
            calculatedTotal -= (calculatedTotal * voucher.getValue()) / 100;
        }
        System.out.println(calculatedTotal);


        if (request.getTotal() <= 0
                || request.getTotal() != calculatedTotal
        ) {
            return OrderResponse.builder()
                    .status("Invalid total value")
                    .order(null)
                    .orderDetails(null)
                    .build();
        }

        // Create and save order
        Order order = Order.builder()
                .orderDate(new Date())
                .total(calculatedTotal)
                .orderStatus(OrderStatus.PENDING)
                .customer(user)
                .customerPhone(request.getCustomerPhone())
                .customerName(request.getCustomerName())
                .customerAddress(request.getCustomerAddress())
                .voucher(voucher)
                .build();
        orderRepo.save(order);

        // Handle order details and update inventory
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(order);
            orderDetailRepo.save(orderDetail);

            // Update item buy count and quantity
            Item item = orderDetail.getItem();
            item.setQuantityCount((item.getQuantityCount() == null ? 0 : item.getQuantityCount()) + orderDetail.getQuantity());
            item.setQuantity(item.getQuantity() - orderDetail.getQuantity());
            itemRepo.save(item);

            // Update shop wallet
            User shop = item.getUser();
            shop.setUserWallet(shop.getUserWallet() + orderDetail.getPriceForShop());
            userRepo.save(shop);
        }

        return OrderResponse.builder()
                .status("Order Created Successfully with Order Details")
                .order(order)
                .orderDetails(orderDetails)
                .build();
    }


    @Override
    public Page<OrderDetailDTO> getOrderDetailsByItemUserId(int userId, Pageable pageable) {
        return orderDetailRepo.findByItemUserId(userId, pageable)
                .map(OrderMapper::toOrderDetailDTO);
    }

    @Override
    public Page<OrderDTO> getOrderForCustomer(int id, Pageable pageable) {
        return orderRepo.findByCustomerUsersID(id, pageable)
                .map(OrderMapper::toOrderDTO);
    }

    @Override
    public long countOrdersByUserId(int userId) {
        return  orderDetailRepo.countByItemUserUsersID(userId);
    }

    @Override
    public long countOrdersByOrderStatus(OrderStatus orderStatus) {
        return orderRepo.countByOrderStatus(orderStatus);
    }

    @Override
    public long countOrdersByOrderStatusAndShopOwnerId(OrderStatus orderStatus, int shopOwnerId) {
        return orderRepo.countByOrderStatusAndShopOwnerId(orderStatus, shopOwnerId);
    }
//    @Override
//    public TotalPriceForShopResponse getTotalPriceForShopByUserId(int userId) {
//        Float totalPriceForShop = orderDetailRepo.findTotalPriceForShopByUserIdAndCurrentMonthAndDelivered(userId);
//        if (totalPriceForShop == null) {
//            totalPriceForShop = 0f;
//        }
//        Calendar calendar = Calendar.getInstance();
//        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
//        int year = calendar.get(Calendar.YEAR);
//        return new TotalPriceForShopResponse(totalPriceForShop, month, year);
//    }
@Override
public TotalPriceForShopResponse getTotalPriceForShopByUserId(int userId, int month, int year) {
    Float totalPriceForShop = orderDetailRepo.findTotalPriceForShopByUserIdAndMonthAndYearAndDelivered(userId, month, year);
    if (totalPriceForShop == null) {
        totalPriceForShop = 0f;
    }
    return new TotalPriceForShopResponse(totalPriceForShop, month, year);
}
    @Override
    public TotalPriceForAdminResponse getTotalPriceForAdmin(int month, int year) {
        Float totalPriceForAdmin = orderDetailRepo.findTotalPriceForAdminByMonthAndYearAndDelivered(month, year);
        if (totalPriceForAdmin == null) {
            totalPriceForAdmin = 0f;
        }
        return new TotalPriceForAdminResponse(totalPriceForAdmin, month, year);
    }
    @Override
    public List<DailyOrderCountResponse> getOrderCountByShopAndMonthAndYear(int userId, int month, int year) {
        List<Object[]> results = orderRepo.countOrdersByShopAndMonthAndYearGroupedByDay(userId, month, year);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        Map<Integer, Long> orderCountMap = new HashMap<>();

        for (Object[] result : results) {
            int day = (int) result[0];
            long count = (long) result[1];
            orderCountMap.put(day, count);
        }

        List<DailyOrderCountResponse> dailyOrderCounts = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            long count = orderCountMap.getOrDefault(day, 0L);
            dailyOrderCounts.add(new DailyOrderCountResponse(day, count));
        }

        return dailyOrderCounts;
    }
    @Override
    public List<DailyOrderCountResponse> getOrderCountByMonthAndYearForAdmin(int month, int year) {
        List<Object[]> results = orderRepo.countOrdersByMonthAndYearGroupedByDayForAdmin(month, year);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        Map<Integer, Long> orderCountMap = new HashMap<>();

        for (Object[] result : results) {
            int day = (int) result[0];
            long count = (long) result[1];
            orderCountMap.put(day, count);
        }

        List<DailyOrderCountResponse> dailyOrderCounts = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            long count = orderCountMap.getOrDefault(day, 0L);
            dailyOrderCounts.add(new DailyOrderCountResponse(day, count));
        }

        return dailyOrderCounts;
    }
//    @Override
//    public List<DailyPriceForShopResponse> getDailyPriceForShopByUserId(int userId, int month, int year) {
//        List<Object[]> results = orderRepo.calculateTotalPriceForShopByDay(userId, month, year);
//        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
//        Map<Integer, Double> priceForShopMap = new HashMap<>();
//
//        for (Object[] result : results) {
//            int day = (int) result[0];
//            double totalPriceForShop = ((Number) result[1]).doubleValue();  // Safe cast to Double
//            priceForShopMap.put(day, totalPriceForShop);
//        }
//
//        List<DailyPriceForShopResponse> dailyPrices = new ArrayList<>();
//        for (int day = 1; day <= daysInMonth; day++) {
//            double totalPriceForShop = priceForShopMap.getOrDefault(day, 0.0);
//            dailyPrices.add(new DailyPriceForShopResponse(day, (float) totalPriceForShop));  // Convert to float for response
//        }
//
//        return dailyPrices;
//    }
//@Override
//public List<DailyPriceForShopResponse> getDailyPriceForShopByUserId(int userId, int month, int year) {
//    List<Object[]> results = orderRepo.calculateTotalPriceForShopByDay(userId, month, year);
//    int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
//    Map<Integer, Double> priceForShopMap = new HashMap<>();
//
//    for (Object[] result : results) {
//        int day = (int) result[0];
//        double totalPriceForShop = ((Number) result[1]).doubleValue();  // Safe cast to double
//        priceForShopMap.put(day, totalPriceForShop);
//    }
//
//    List<DailyPriceForShopResponse> dailyPrices = new ArrayList<>();
//    for (int day = 1; day <= daysInMonth; day++) {
//        double totalPriceForShop = priceForShopMap.getOrDefault(day, 0.0);
//        dailyPrices.add(new DailyPriceForShopResponse(day, totalPriceForShop));  // Keep as double
//    }
//    return dailyPrices;
//}
@Override
public List<DailyPriceForShopResponse> getDailyPriceForShopByUserId(int userId, int month, int year) {
    List<Object[]> results = orderDetailRepo.calculateTotalPriceForShopByDay(userId, month, year);
    int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
    Map<Integer, Double> priceForShopMap = new HashMap<>();

    for (Object[] result : results) {
        int day = (int) result[0];
        double totalPriceForShop = ((Number) result[1]).doubleValue();  // Safe cast to double
        priceForShopMap.put(day, totalPriceForShop);
    }

    List<DailyPriceForShopResponse> dailyPrices = new ArrayList<>();
    for (int day = 1; day <= daysInMonth; day++) {
        double totalPriceForShop = priceForShopMap.getOrDefault(day, 0.0);
        dailyPrices.add(new DailyPriceForShopResponse(day, totalPriceForShop));  // Keep as double
    }

    return dailyPrices;
}
    @Override
    public List<DailyPriceForAdminResponse> getDailyPriceForAdmin(int month, int year) {
        List<Object[]> results = orderDetailRepo.calculateTotalPriceForAdminByDay(month, year);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        Map<Integer, Double> priceForAdminMap = new HashMap<>();

        for (Object[] result : results) {
            int day = (int) result[0];
            double totalPriceForAdmin = ((Number) result[1]).doubleValue();  // Safe cast to double
            priceForAdminMap.put(day, totalPriceForAdmin);
        }

        List<DailyPriceForAdminResponse> dailyPrices = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            double totalPriceForAdmin = priceForAdminMap.getOrDefault(day, 0.0);
            dailyPrices.add(new DailyPriceForAdminResponse(day, totalPriceForAdmin));  // Keep as double
        }

        return dailyPrices;
    }


    @Override
    public OrderResponse changeStatus(int orderId, OrderStatus status) {
        var order = orderRepo.findByOrderId(orderId).orElse(null);
        if (order == null) {
            return  OrderResponse.builder()
                    .status("Order Not Found")
                    .order(null)
                    .build();
        }
        order.setOrderStatus(status);
        orderRepo.save(order);
        return OrderResponse.builder()
                .status("Status Updated Successfully")
                .order(order)
                .build();

    }
    @Override
    public List<DailyOrderCountResponse> getCompletedOrderCountByDayWithItemType(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<DailyOrderCountResponse> dailyOrderCounts = new ArrayList<>();

        // Initialize the list with all days of the month set to zero count
        for (int day = 1; day <= daysInMonth; day++) {
            dailyOrderCounts.add(new DailyOrderCountResponse(day, 0));
        }

        // Get the actual order counts from the repository
        List<Object[]> results = orderRepo.countCompletedOrdersByDayWithItemType(month, year);

        // Update the count for each day based on the query results
        for (Object[] result : results) {
            int day = (int) result[0];
            long count = (long) result[1];
            dailyOrderCounts.set(day - 1, new DailyOrderCountResponse(day, count));
        }

        return dailyOrderCounts;
    }

    @Override
    public List<DailyPriceForAdminResponse> getTotalPriceByDayWithItemType(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<DailyPriceForAdminResponse> dailyTotalPrices = new ArrayList<>();

        // Initialize the list with all days of the month set to zero total price
        for (int day = 1; day <= daysInMonth; day++) {
            dailyTotalPrices.add(new DailyPriceForAdminResponse(day, 0.0));
        }

        // Get the actual total prices from the repository
        List<Object[]> results = orderRepo.sumTotalPriceByDayWithItemType(month, year);

        // Update the total price for each day based on the query results
        for (Object[] result : results) {
            int day = (int) result[0];
            double totalPrice = (double) result[1];
            dailyTotalPrices.set(day - 1, new DailyPriceForAdminResponse(day, totalPrice));
        }

        return dailyTotalPrices;
    }

    @Override
    public List<DailyPriceForShopResponse> getTotalPriceByDayForShop(int userId, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<DailyPriceForShopResponse> dailyTotalPrices = new ArrayList<>();

        // Initialize the list with all days of the month set to zero total price
        for (int day = 1; day <= daysInMonth; day++) {
            dailyTotalPrices.add(new DailyPriceForShopResponse(day, 0.0));
        }

        // Get the actual total prices from the repository
        List<Object[]> results = orderRepo.sumTotalPriceByDayForShop(userId, month, year);

        // Update the total price for each day based on the query results
        for (Object[] result : results) {
            int day = (int) result[0];
            double totalPrice = (double) result[1];
            dailyTotalPrices.set(day - 1, new DailyPriceForShopResponse(day, totalPrice));
        }

        return dailyTotalPrices;
    }


}
