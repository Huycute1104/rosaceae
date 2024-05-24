package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.model.Cart;
import com.example.rosaceae.repository.CartRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImplement implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {

        int userId = request.getUserId();
        int ItemId = request.getItemId();
        var user = userRepo.findUserByUsersID(userId).orElse(null);
        var item = itemRepo.findByItemId(ItemId).orElse(null);
//        int quantity = request.getQuantity();

        if (item == null) {
            return CartResponse.builder()
                    .status("Item not found !")
                    .cart(null)
                    .build();
        } else if (user != null && (user.getRole() == Role.CUSTOMER)) {
            Optional<Cart> existingCart = cartRepo.findByUserAndItem(user, item);
            if (existingCart.isPresent()) {
                existingCart.get().setQuantity(existingCart.get().getQuantity() + 1);
                cartRepo.save(existingCart.get());
                return CartResponse.builder()
                        .status("Add to cart successfully.")
                        .cart(existingCart.get())
                        .build();
            }else {
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setItem(item);
                cart.setQuantity(1);
                cartRepo.save(cart);
                return CartResponse.builder()
                        .status("Add to cart successfully")
                        .cart(cart)
                        .build();
            }
        } else {
            return CartResponse.builder()
                    .status("Add to cart fail")
                    .cart(null)
                    .build();
        }
    }
}
