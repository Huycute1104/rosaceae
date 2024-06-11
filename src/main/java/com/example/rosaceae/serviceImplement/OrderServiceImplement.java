package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.Order;
import com.example.rosaceae.model.OrderDetail;
import com.example.rosaceae.repository.OrderDetailRepo;
import com.example.rosaceae.repository.OrderRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.repository.VoucherRepo;
import com.example.rosaceae.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
}
