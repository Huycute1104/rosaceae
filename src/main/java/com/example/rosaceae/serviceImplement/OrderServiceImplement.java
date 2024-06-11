package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.model.Order;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.repository.VoucherRepo;
import com.example.rosaceae.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImplement implements OrderService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private VoucherRepo voucherRepo;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public OrderResponse CreateOrder(CreateOrderRequest request) {
//        var user = userRepo.findUserByUsersID(request.getCustomerId()).orElse(null);
//        if (user == null) {
//            return OrderResponse.builder()
//                    .status("User Not Found")
//                    .order(null)
//                    .build();
//        }
//        var voucher = voucherRepo.findVoucherByVoucherId(request.getVoucherId()).orElse(null);
//        if(voucher == null){
//            return OrderResponse.builder()
//                    .status("Voucher Not Found")
//                    .order(null)
//                    .build();
//        }
//        float total = request.getTotal();
//        if (total <= 0) {
//            return OrderResponse.builder()
//                    .status("Total must be a positive value.")
//                    .order(null)
//                    .build();
//        }
//        Order order = Order.builder()
//                .orderDate(new Date())
//                .
//                .build();
return null;

    }
}
