package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.VoucherRequest.CreateVoucherRequest;
import com.example.rosaceae.dto.Response.VoucherResponse.CreateVoucherResponse;
import com.example.rosaceae.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VoucherService {
    Page<Voucher> getVouchers(Pageable pageable);
    public Optional<Voucher> getVoucherById(int id);
    public CreateVoucherResponse CreateVoucher(CreateVoucherRequest request);
}
