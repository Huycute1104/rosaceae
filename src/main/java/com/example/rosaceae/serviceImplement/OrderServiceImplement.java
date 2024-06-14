package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.OrderDTO;
import com.example.rosaceae.dto.Data.OrderDetailDTO;
import com.example.rosaceae.dto.Data.OrderMapper;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.enums.Fee;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.*;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

            float itemTotal = item.getItemPrice() * itemRequest.getQuantity();
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

        if (request.getTotal() <= 0
//                || request.getTotal() != calculatedTotal
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
}
