package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
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

            if (body.getItems() == null || body.getItems().isEmpty()) {
                response.put("error", -1);
                response.put("message", "No items provided");
                response.set("data", null);
                return ResponseEntity.status(400).body(response);
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
                    .customerAddress(body.getCustomerAddress())
                    .customerName(body.getCustomerName())
                    .customerPhone(body.getCustomerPhone())
                    .voucher(voucher)
                    .build();
            orderRepo.save(order);

            // Handle order details
            float fee = Fee.SHOP_FEE.getFee() / 100;
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (CreateOrderRequest.OrderItemRequest itemRequest : body.getItems()) {
                var item = itemRepo.findById(itemRequest.getItemId()).orElse(null);
                if (item == null) {
                    response.put("error", -1);
                    response.put("message", "Item with ID " + itemRequest.getItemId() + " not found");
                    response.set("data", null);
                    return ResponseEntity.status(404).body(response);
                }

                if (item.getQuantity() < itemRequest.getQuantity()) {
                    response.put("error", -1);
                    response.put("message", "Insufficient quantity for item with ID " + itemRequest.getItemId());
                    response.set("data", null);
                    return ResponseEntity.status(400).body(response);
                }

                float discount = (float) item.getDiscount() /100;
                float itemDiscount = item.getItemPrice() * discount;
                float itemTotal = (item.getItemPrice()-itemDiscount) * itemRequest.getQuantity();
                OrderDetail orderDetail = OrderDetail.builder()
                        .item(item)
                        .quantity(itemRequest.getQuantity())
                        .price(itemTotal)
                        .priceForShop(itemTotal - (itemTotal * fee))
                        .order(order)
                        .build();
                orderDetails.add(orderDetail);

                int currentBuyCount = (item.getQuantityCount() == null) ? 0 : item.getQuantityCount();
                item.setQuantityCount(currentBuyCount + itemRequest.getQuantity());
                item.setQuantity(item.getQuantity() - itemRequest.getQuantity());
                itemRepo.save(item);

                User shop = orderDetail.getItem().getUser();
                shop.setUserWallet(shop.getUserWallet() + orderDetail.getPriceForShop());
                userRepo.save(shop);
            }

            // Save order details
            orderDetailRepo.saveAll(orderDetails);

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

            return ResponseEntity.status(200).body(response);

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
