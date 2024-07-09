package com.example.rosaceae.serviceImplement;


import com.example.rosaceae.auth.AuthenticationResponse;
import com.example.rosaceae.dto.Data.*;
import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.CategoryRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.ItemTypeRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemTypeRepo itemTypeRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    public boolean emailVerify(String token) {
        User user = userRepo.findUserByVerificationCode(token).get();

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepo.save(user);

            return true;
        }
    }
    @Override
    public UserResponse toggleUserStatus(int userId) {
        // Prevent toggling status for super admin and admin (user IDs 1 and 2)
        if (userId == 1 || userId == 2) {
            return UserResponse.builder().status("Thất bại: Không thể thay đổi trạng thái cho người dùng super admin hoặc admin.").build();
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserStatus(!user.isUserStatus());
        userRepo.save(user);
        return UserResponse.builder().status("Thành công").build();
    }
    @Override
    public UserResponse updateUserDetails(UserRequest updateUserRequest) {
        // Manual validation
        if (updateUserRequest.getAccountName() == null || updateUserRequest.getAccountName().isEmpty()) {
            return UserResponse.builder().status("Thất bại: Account name cannot be blank").build();
        }
        if (updateUserRequest.getPhone() == null || updateUserRequest.getPhone().isEmpty()) {
            return UserResponse.builder().status("Thất bại: Phone cannot be blank").build();
        }
        if (!updateUserRequest.getPhone().matches("\\d+")) {
            return UserResponse.builder().status("Thất bại: Phone must contain only numeric characters").build();
        }
        if (updateUserRequest.getAddress() == null || updateUserRequest.getAddress().isEmpty()) {
            return UserResponse.builder().status("Thất bại: Address cannot be blank").build();
        }

        User userToUpdate = userRepo.findById(updateUserRequest.getUsersID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userToUpdate.setAccountName(updateUserRequest.getAccountName());
        userToUpdate.setPhone(updateUserRequest.getPhone());
        userToUpdate.setAddress(updateUserRequest.getAddress());
        userRepo.save(userToUpdate);

        return UserResponse.builder().status("Cập nhật thành công").build();
    }

    @Override
    public UserResponse changePassword(int userId, String newPassword) {
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return null;
    }

    @Override
    public List<User> searchByAccountNameOrPhone(String keyword) {
        return userRepo.searchByAccountNameOrPhone(keyword);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return userRepo.findById(userId);
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        return userRepo.findUsersByTokensToken(token);
    }

    @Override
    public Page<User> getUser(Specification<User> spec, Pageable pageable) {
        return userRepo.findAll(spec, pageable);
    }

    @Override
    public Optional<UserDTO> getUserDTO(int userId, int page, int size, Double minPrice, Double maxPrice, String categoryName, String itemTypeName) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            Pageable pageable = PageRequest.of(page, size);
            Page<Item> itemPage = itemRepo.findByUserAndFilters(userId, minPrice, maxPrice, categoryName, itemTypeName, pageable);
            Page<ItemDTO> itemDTOPage = itemPage.map(this::convertToDTO);

            var userDTO = UserDTO.builder()
                    .usersID(user.getUsersID())
                    .accountName(user.getAccountName())
                    .email(user.getEmail())
                    .userStatus(user.isUserStatus())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .rate(user.getRate())
                    .userWallet(user.getUserWallet())
                    .role(user.getRole().name())
                    .coverImages(user.getCoverImages())
                    .items(itemDTOPage)
                    .build();

            return Optional.of(userDTO);
        } else {
            return Optional.empty();
        }
    }

    private ItemDTO convertToDTO(Item item) {
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDescription(item.getItemDescription())
                .itemRate(item.getItemRate())
                .commentCount(item.getCommentCount())
                .countUsage(item.getCountUsage())
                .quantity(item.getQuantity())
                .discount(item.getDiscount())
                .itemImages(item.getItemImages().stream()
                        .map(image -> ItemImageDTO.builder()
                                .imageUrl(image.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .category(CategoryDTO.builder()
                        .categoryId(item.getCategory().getCategoryId())
                        .categoryName(item.getCategory().getCategoryName())
                        .build())
                .itemType(ItemTypeDTO.builder()
                        .itemTypeId(item.getItemType().getItemTypeId())
                        .itemTypeName(item.getItemType().getItemTypeName())
                        .build())
                .build();
    }
    public AuthenticationResponse getUserByEmail(String email) {
        // TODO Auto-generated method stub
        var user = userRepo.findByEmail(email).orElseThrow();
        System.out.println(user);
        if(user == null){
            return AuthenticationResponse.builder()
                    .msg("No user found")
                    .status(400)
                    .build();
        }
        else {
            return AuthenticationResponse.builder()
                    .msg("Login successfully")
                    .status(200)
//                .userInfo(userRepo.findUserByEmail(request.getEmail()).orElseThrow())
                    .accessToken("")
                    .refreshToken("")
                    .userInfo(user)
                    .build();
        }
    }
}
