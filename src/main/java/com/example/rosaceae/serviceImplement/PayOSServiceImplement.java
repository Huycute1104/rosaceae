package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.enums.Fee;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.*;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.PayOSService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import com.lib.payos.type.ItemData;
import com.lib.payos.type.PaymentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayOSServiceImplement implements PayOSService {

    @Autowired
    private PayOS payOS;
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @Autowired
    private CartRepo cartRepository;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public ResponseEntity<ObjectNode> createOrderQR(CreatePaymentLinkRequestBody body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            final String description = body.getDescription();
            final String returnUrl = body.getReturnUrl();
            final String cancelUrl = body.getCancelUrl();

            var user = userRepo.findUserByUsersID(body.getCustomerId()).orElse(null);
            if (user == null) {
                response.put("error", -1);
                response.put("message", "User Not Found");
                response.set("data", null);
                return ResponseEntity.status(404).body(response);
            }

            List<Cart> cartItems1 = cartRepository.findByUser(user);
            if (cartItems1 == null || cartItems1.isEmpty()) {
                response.put("error", -1);
                response.put("message", "Cart is empty");
                response.set("data", null);
                return ResponseEntity.status(404).body(response);
            }

            // Calculate total
            float total = body.getTotal();
            if (total <= 0) {
                response.put("error", -1);
                response.put("message", "Total must be a positive value");
                response.set("data", null);
                return ResponseEntity.status(400).body(response);
            }

            // Apply voucher if present
            var voucher = voucherRepo.findVoucherByVoucherId(body.getVoucherId()).orElse(null);
            if (voucher != null) {
                total -= (total * voucher.getValue()) / 100;
            }

            // Create and save order
            Order order = Order.builder()
                    .orderDate(new Date())
                    .total(total)
                    .orderStatus(OrderStatus.PENDING)
                    .customer(user)
                    .voucher(voucher)
                    .build();
            orderRepo.save(order);

            // Handle order details
            float fee = Fee.SHOP_FEE.getFee() / 100;
            List<Cart> cartItems = cartRepository.findByUser(user);
            if (!cartItems.isEmpty()) {
                List<OrderDetail> orderDetails = cartItems.stream().map(cartItem -> {
                    OrderDetail orderDetail = OrderDetail.builder()
                            .item(cartItem.getItem())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getItem().getItemPrice() * cartItem.getQuantity())
                            .priceForShop(cartItem.getItem().getItemPrice() * cartItem.getQuantity() - (cartItem.getItem().getItemPrice() * cartItem.getQuantity() * fee))
                            .order(order)
                            .build();
                    orderDetailRepo.save(orderDetail);

                    return orderDetail;
                }).collect(Collectors.toList());

                // Generate order code
                String currentTimeString = String.valueOf(new Date().getTime());
                int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

                List<ItemData> itemList = orderDetails.stream().map(orderDetail -> new ItemData(
                        orderDetail.getItem().getItemName(),
                        orderDetail.getQuantity(),
                        (int) orderDetail.getPrice()
                )).collect(Collectors.toList());

                PaymentData paymentData = new PaymentData(orderCode, (int) order.getTotal(), description, itemList, cancelUrl, returnUrl);

                JsonNode data = payOS.createPaymentLink(paymentData);

                response.put("error", 0);
                response.put("message", "success");
                response.set("data", data);

                return ResponseEntity.ok(response);
            } else {
                response.put("error", -1);
                response.put("message", "Cart is empty");
                response.set("data", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Internal Server Error");
            response.set("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }


    @Override
    public ResponseEntity<PayOSSuccess> Success(int orderId) {
        var order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(404).body(new PayOSSuccess("Order Not Found"));
        } else {
            order.setOrderStatus(OrderStatus.SHIPPED);
            orderRepo.save(order);
            return ResponseEntity.ok(new PayOSSuccess("Conform Order Success"));
        }
    }

    @Override
    public ResponseEntity<PayOSCancel> Cancel(int orderId) {
        var order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(404).body(new PayOSCancel("Order Not Found"));
        } else {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);
            return ResponseEntity.ok(new PayOSCancel("Cancel Order Success"));
        }
    }
}
