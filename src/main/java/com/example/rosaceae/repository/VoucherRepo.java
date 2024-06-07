package com.example.rosaceae.repository;

import com.example.rosaceae.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findVoucherByVoucherId(int id);
    Optional<Voucher> findVoucherByVoucherName(String voucherName);
}
