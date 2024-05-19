package com.example.rosaceae.serviceImplement;


import com.example.rosaceae.dto.Request.UserRequest.UserRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepo.findAll(pageable);
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
}
