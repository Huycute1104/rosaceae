package com.example.rosaceae.repository;

import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsersID(int userid);
    Optional<User> findUserByVerificationCode(String token);
    @Query("SELECT u FROM User u WHERE u.accountName LIKE %:keyword% OR u.phone LIKE %:keyword%")
    List<User> searchByAccountNameOrPhone(@Param("keyword") String keyword);
    Optional<User> findUsersByTokensToken(String token);
}
