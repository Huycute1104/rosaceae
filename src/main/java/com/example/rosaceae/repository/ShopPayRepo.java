package com.example.rosaceae.repository;

import com.example.rosaceae.model.ShopPay;
import com.example.rosaceae.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShopPayRepo extends CrudRepository<ShopPay, Integer> {
    Optional<ShopPay> findByMonthAndYearAndUser(int month, int yearA, User user);
    Page<ShopPay> findByMonthAndYear(int month, int year, Pageable pageable);

}
