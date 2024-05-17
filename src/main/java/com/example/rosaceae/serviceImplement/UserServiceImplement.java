package com.example.rosaceae.serviceImplement;


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
}
