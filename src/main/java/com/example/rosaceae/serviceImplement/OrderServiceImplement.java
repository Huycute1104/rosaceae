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

        List<Cart> cartItems1 = cartRepository.findByUser(user);
        if(cartItems1 == null|| cartItems1.isEmpty()){
            return OrderResponse.builder()
                    .status("Cart is empty")
                    .order(null)
                    .orderDetails(null)
                    .build();
        }

        // Calculate total
        float total = request.getTotal();
        if (total <= 0) {
            return OrderResponse.builder()
                    .status("Total must be a positive value.")
                    .order(null)
                    .orderDetails(null)
                    .build();
        }

        // Apply voucher if present
        var voucher = voucherRepo.findVoucherByVoucherId(request.getVoucherId()).orElse(null);
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

                // Update item buy count
                Item item = cartItem.getItem();
                int currentBuyCount = (item.getQuantityCount() == null) ? 0 : item.getQuantityCount();
                item.setQuantityCount(currentBuyCount + cartItem.getQuantity());
                item.setQuantity(item.getQuantity() - cartItem.getQuantity());
                itemRepo.save(item);

                // Update shop wallet
                User shop = orderDetail.getItem().getUser();
                shop.setUserWallet(shop.getUserWallet() + orderDetail.getPriceForShop());
                userRepo.save(shop);

                // Clear cart item
                cartRepository.delete(cartItem);

                return orderDetail;
            }).collect(Collectors.toList());

            return OrderResponse.builder()
                    .status("Order Created Successfully with Order Details")
                    .order(order)
                    .orderDetails(orderDetails)
                    .build();
        } else {
            return OrderResponse.builder()
                    .status("Cart is empty")
                    .order(order)
                    .orderDetails(null)
                    .build();
        }
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
