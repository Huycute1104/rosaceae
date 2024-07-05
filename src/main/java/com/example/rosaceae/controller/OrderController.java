package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.OrderDTO;
import com.example.rosaceae.dto.Data.OrderDetailDTO;
import com.example.rosaceae.dto.Request.OrderDetailRequest.OrderDetailRequest;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Response.OrderDetailResponse.OrderDetailResponse;
import com.example.rosaceae.dto.Response.OrderResponse.DailyOrderCountResponse;
import com.example.rosaceae.dto.Response.OrderResponse.OrderResponse;
import com.example.rosaceae.dto.Response.OrderResponse.TotalPriceForShopResponse;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.service.OrderDetailService;
import com.example.rosaceae.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService  orderDetailService;

    @PostMapping("")
//    @PreAuthorize("hasAuthority('customer:create')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request ){
        return ResponseEntity.ok(orderService.createOrderWithDetails(request));
    }

    @PostMapping("/orderdetail")
//    @PreAuthorize("hasAuthority('customer:create')")
    public ResponseEntity<OrderDetailResponse> createOrderDetail(@RequestBody OrderDetailRequest request ){
        return ResponseEntity.ok(orderDetailService.createOrderDetail(request));
    }

    @GetMapping("/customer/{id}")
    public Page<OrderDTO> getOrdersForCustomer(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrderForCustomer(id, pageable);
    }
    @GetMapping("/shop/{userId}")
    public Page<OrderDetailDTO> getOrderDetailsByItemUserId(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrderDetailsByItemUserId(userId, pageable);
    }

    @GetMapping("/orderCount/{shopId}")
    public long countOrdersByUserId(@PathVariable int shopId) {
        return orderService.countOrdersByUserId(shopId);
    }
    @GetMapping("/countByOrderStatus")
    public long countOrdersByOrderStatus(@RequestParam OrderStatus orderStatus) {
        return orderService.countOrdersByOrderStatus(orderStatus);
    }
    @GetMapping("/countOrderByStatusForShop")
    public long countOrdersByOrderStatusAndShopOwnerId(@RequestParam OrderStatus orderStatus, @RequestParam int shopOwnerId) {
        return orderService.countOrdersByOrderStatusAndShopOwnerId(orderStatus, shopOwnerId);
    }
//    @GetMapping("/{userId}/total-price-for-shop")
//    public TotalPriceForShopResponse getTotalPriceForShopByUserId(@PathVariable int userId) {
//        return orderService.getTotalPriceForShopByUserId(userId);
//    }
@GetMapping("/total-price-for-shop")
public TotalPriceForShopResponse getTotalPriceForShopByUserId(
        @RequestParam int userId,
        @RequestParam int month,
        @RequestParam int year) {
    return orderService.getTotalPriceForShopByUserId(userId, month, year);
}
    @GetMapping("/order-count-by-day")
    public ResponseEntity<List<DailyOrderCountResponse>> getOrderCountByShopAndMonthAndYear(
            @RequestParam int userId,
            @RequestParam int month,
            @RequestParam int year) {
        List<DailyOrderCountResponse> orderCounts = orderService.getOrderCountByShopAndMonthAndYear(userId, month, year);
        return ResponseEntity.ok(orderCounts);
    }
}
