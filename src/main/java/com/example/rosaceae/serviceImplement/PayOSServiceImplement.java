package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.PayOS.CreatePaymentLinkRequestBody;
import com.example.rosaceae.dto.PayOS.PayOSCancel;
import com.example.rosaceae.dto.PayOS.PayOSSuccess;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.Order;
import com.example.rosaceae.repository.OrderRepo;
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

@Service
public class PayOSServiceImplement implements PayOSService {

    @Autowired
    private PayOS payOS;
    @Autowired
    private OrderRepo orderRepo;

    @Override
    public ResponseEntity<ObjectNode> createOrderQR(CreatePaymentLinkRequestBody body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            final String productName = body.getProductName();
            final String description = body.getDescription();
            final String returnUrl = body.getReturnUrl();
            final String cancelUrl = body.getCancelUrl();
            final int price = body.getPrice();

            // Generate order code
            String currentTimeString = String.valueOf(new Date().getTime());
            int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = new ItemData(productName, 1, price);
            List<ItemData> itemList = new ArrayList<>();
            itemList.add(item);

            PaymentData paymentData = new PaymentData(orderCode, price, description, itemList, cancelUrl, returnUrl);

            JsonNode data = payOS.createPaymentLink(paymentData);

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Override
    public ResponseEntity<PayOSSuccess> Success(int orderId) {
        var order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(404).body(new PayOSSuccess("Order Not Found"));
        }else {
            order.setOrderStatus(OrderStatus.SHIPPED);
            return ResponseEntity.ok(new PayOSSuccess("Conform Order Success"));
        }
    }

    @Override
    public ResponseEntity<PayOSCancel> Cancel(int orderId) {
        var order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(404).body(new PayOSCancel("Order Not Found"));
        }else {
            order.setOrderStatus(OrderStatus.CANCELLED);
            return ResponseEntity.ok(new PayOSCancel("Cancel Order Success"));
        }
    }
}
