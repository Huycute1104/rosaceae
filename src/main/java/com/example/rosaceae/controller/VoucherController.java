package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.VoucherRequest.CreateVoucherRequest;
import com.example.rosaceae.dto.Response.VoucherResponse.CreateVoucherResponse;
import com.example.rosaceae.model.Voucher;
import com.example.rosaceae.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("")
    public Page<Voucher> getVouchers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return voucherService.getVouchers(PageRequest.of(page, size));
    }
    @GetMapping("/{id}")
    public Optional<Voucher> getVoucherById(@PathVariable int id) {
        return voucherService.getVoucherById(id);
    }
    @PostMapping("")
//    @PreAuthorize("hasAuthority('admin:create')")
    public CreateVoucherResponse createVoucher(@RequestBody CreateVoucherRequest request) {
        return voucherService.CreateVoucher(request);
    }
}
