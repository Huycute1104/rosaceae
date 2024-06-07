package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.UserDTO;
import com.example.rosaceae.model.User;
import com.example.rosaceae.service.ShopService;
import com.example.rosaceae.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor

public class ShopController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
//    @GetMapping("")
//    public Page<User> getAllUser(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String role
//    ) {
//        Specification<User> spec = Specification.where(null);
//        if (role != null) {
//            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), role));
//        }
//
//        Pageable pageable = PageRequest.of(page, size);
//        return userService.getUser(spec, pageable);
//    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getUserByID(@PathVariable int id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return userService.getUserDTO(id, page, size);
    }

    @GetMapping("")
    public Page<User> getAllShops(Pageable pageable) {
        return shopService.getAllUser(pageable);
    }
}
