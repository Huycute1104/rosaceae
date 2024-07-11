package com.example.rosaceae.enums;

public enum OrderStatus {
    PENDING,     // Đơn hàng đang chờ xử lý
    SHIPPED,     // Đơn hàng đã được giao đi
    DELIVERED,   // Đơn hàng đã được giao thành công
    CANCELLED,   // Đơn hàng đã bị hủy bỏ
    BOOKING_COMPLETED;
}
