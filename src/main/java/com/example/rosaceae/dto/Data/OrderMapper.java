package com.example.rosaceae.dto.Data;

import com.example.rosaceae.model.Order;
import com.example.rosaceae.model.OrderDetail;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toOrderDTO(Order order) {
        Set<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
                .map(OrderMapper::toOrderDetailDTO)
                .collect(Collectors.toSet());

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetailDTOs)
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerAddress(order.getCustomerAddress())
                .build();
    }

    public static OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail) {
        return OrderDetailDTO.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .itemId(orderDetail.getItem().getItemId())
                .itemName(orderDetail.getItem().getItemName())
                .CustomerName(orderDetail.getOrder().getCustomerName())
                .itemTypeId(orderDetail.getItem().getItemType().getItemTypeId())
                .status(orderDetail.getOrder().getOrderStatus())
                .itemImages(orderDetail.getItem().getItemImages().get(1).getImageUrl())
                .orderDate(orderDetail.getOrder().getOrderDate())
                .build();
    }
}
