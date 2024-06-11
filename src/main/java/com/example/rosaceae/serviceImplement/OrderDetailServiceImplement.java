package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.OrderDetailRequest.OrderDetailRequest;
import com.example.rosaceae.dto.Response.OrderDetailResponse.OrderDetailResponse;
import com.example.rosaceae.enums.Fee;
import com.example.rosaceae.model.Cart;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.OrderDetail;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.rosaceae.enums.Fee.SHOP_FEE;

@Service
public class OrderDetailServiceImplement implements OrderDetailService {
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @Autowired
    private CartRepo cartRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
//    @Autowired
//    private FeeRepo feeRepo;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        float fee = Fee.SHOP_FEE.getFee() / 100;
        int orderId = request.getOrderId();
        var order = orderRepo.findByOrderId(orderId).orElse(null);
        if (order != null) {
            int customer = request.getCustomerId();
            var user = userRepo.findUserByUsersID(customer).orElse(null);
            if (user != null && user.getUsersID() == order.getCustomer().getUsersID()) {
                List<Cart> cartItems = cartRepository.findByUser(user);
                if (!cartItems.isEmpty()) {
                    for (Cart cartItem : cartItems) {
                        OrderDetail orderDetail = OrderDetail.builder()
                                .item(cartItem.getItem())
                                        .quantity(cartItem.getQuantity())
                                        .price(cartItem.getItem().getItemPrice() * cartItem.getQuantity())
                                        .priceForShop(cartItem.getItem().getItemPrice()*cartItem.getQuantity()-(cartItem.getItem().getItemPrice()*cartItem.getQuantity()*fee))
                                .order(order)
                                .build();
                        orderDetailRepo.save(orderDetail);
                        Item item = cartItem.getItem();

                        int currentBuyCount = (item.getQuantityCount() == null) ? 0 : item.getQuantityCount();
                        item.setQuantityCount(currentBuyCount + cartItem.getQuantity());
                        itemRepo.save(item);
                        cartRepository.delete(cartItem);
                        User shop = orderDetail.getItem().getUser();
                        shop.setUserWallet(shop.getUserWallet() + orderDetail.getPriceForShop());
                        userRepo.save(shop);
                    }
                    return OrderDetailResponse.builder()
                            .status("Create order detail successful")
                            .build();
                } else {
                    return OrderDetailResponse.builder()
                            .status("Cart is empty")
                            .build();
                }

            } else {
                return OrderDetailResponse.builder()
                        .status("User not found")
                        .build();
            }

        } else {
            return OrderDetailResponse.builder()
                    .status("Order not found")
                    .build();
        }
    }
}
