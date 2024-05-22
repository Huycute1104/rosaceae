package com.example.rosaceae.repository;

import com.example.rosaceae.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsersID(int userid);
    Optional<User> findUserByVerificationCode(String token);
}
