package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.CartRequest.AddToCartRequest;
import com.example.rosaceae.dto.Response.CartResponse.CartResponse;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.model.Cart;
import com.example.rosaceae.model.ItemType;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.CartRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.ItemTypeRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ItemTypeRepo itemTypeRepo;

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
            } else {
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

    @Override
    public Page<Cart> viewCarOfCustomer(int customerId, int type, Pageable pageable) {
        try {
            User user = userRepo.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            ItemType itemType = itemTypeRepo.findById(type)
                    .orElseThrow(() -> new IllegalArgumentException("Item type not found"));

            return cartRepo.findByUserIdAndItemTypeId(customerId, type, pageable);
        } catch (Exception ex) {
            // Log the exception
            ex.printStackTrace();
            throw new RuntimeException("Failed to fetch carts for the customer");
        }
    }

    @Override
    public CartResponse removeCart(int id) {
        if (cartRepo.existsById(id)) {
            cartRepo.deleteById(id);
            return CartResponse.builder()
                    .status("Item removed from cart successfully").build();
        } else {
            return CartResponse.builder()
                    .status("Item does not exist").build();
        }

    }

    @Override
    public CartResponse updateCartItem(int id, int quantity) {
        var item = itemRepo.findByItemId(id).orElse(null);
        if (item == null) {

        }
        return null;
    }


}
