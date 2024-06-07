package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.VoucherRequest.CreateVoucherRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.VoucherResponse.CreateVoucherResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.User;
import com.example.rosaceae.model.Voucher;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.repository.VoucherRepo;
import com.example.rosaceae.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class VoucherServiceImplement implements VoucherService {
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public Page<Voucher> getVouchers(Pageable pageable) {
        return voucherRepo.findAll(pageable);
    }

    @Override
    public Optional<Voucher> getVoucherById(int id) {
        return voucherRepo.findVoucherByVoucherId(id);
    }

    @Override
    public CreateVoucherResponse CreateVoucher(CreateVoucherRequest request) {
        String name = request.getVoucherName();
        int value = request.getValue();
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();
        User user = userRepo.findUserByUsersID(request.getUserid()).orElse(null);

        if (user == null) {
            return CreateVoucherResponse.builder()
                    .status("User not found")
                    .voucher(null)
                    .build();
        }

        if (startDate == null || endDate == null || startDate.after(endDate) || startDate.before(new Date())) {
            return CreateVoucherResponse.builder()
                    .status("Invalid date range. Ensure start date is before end date and both are in the future.")
                    .voucher(null)
                    .build();
        }

        if (value > 100 || value < 0) {
            return CreateVoucherResponse.builder()
                    .status("Value is out of range")
                    .voucher(null)
                    .build();
        }

        if (!isValidName(name)) {
            return CreateVoucherResponse.builder()
                    .status("The Voucher Name must be between 3 and 20 characters")
                    .voucher(null)
                    .build();
        }

        Voucher existingVoucher = voucherRepo.findVoucherByVoucherName(name).orElse(null);
        if (existingVoucher != null) {
            return CreateVoucherResponse.builder()
                    .status("Voucher already exists")
                    .voucher(null)
                    .build();
        }

        Voucher voucher = Voucher.builder()
                .voucherName(name)
                .value(value)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .build();

        voucherRepo.save(voucher);

        return CreateVoucherResponse.builder()
                .status("Create new voucher successfully")
                .voucher(voucher)
                .build();
    }

    private boolean isValidName(String name) {
        return name != null && name.length() <= 20 && name.length() >= 3;
    }
}
