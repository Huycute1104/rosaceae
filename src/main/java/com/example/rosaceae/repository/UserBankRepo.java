package com.example.rosaceae.repository;

import com.example.rosaceae.model.UserBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBankRepo extends JpaRepository<UserBank, Integer> {
    Optional<UserBank> findByUserUsersID(int id);
}
