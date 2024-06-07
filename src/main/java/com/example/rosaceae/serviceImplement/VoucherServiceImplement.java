package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.VoucherRequest.CreateVoucherRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.VoucherResponse.CreateVoucherResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.Voucher;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.repository.VoucherRepo;
import com.example.rosaceae.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        var user = userRepo.findUserByUsersID(request.getUserid()).orElse(null);
        if(user == null){
            return CreateVoucherResponse.builder()
                    .status("User not found")
                    .voucher(null)
                    .build();
        }
        if (startDate == null || endDate == null || !isValidDateRange(startDate, endDate)) {
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
        // Check exist voucher
        var existingVoucher = voucherRepo.findVoucherByVoucherName(name).orElse(null);
        if (existingVoucher == null) {
            if (isValidName(name)) {
                Voucher voucher = Voucher.builder()
                        .voucherName(name)
                        .value(value)
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .user(user)
                        .build();
                voucherRepo.save(voucher);
                return CreateVoucherResponse.builder()
                        .status("Create new voucher successfully")
                        .voucher(voucher)
                        .build();
            } else {
                return CreateVoucherResponse.builder()
                        .status("The Voucher Name must be between 3 and 20 characters ")
                        .voucher(null)
                        .build();
            }
        } else {
            return CreateVoucherResponse.builder()
                    .status("Voucher already exists")
                    .voucher(null)
                    .build();
        }
    }



    private boolean isValidName(String name) {
        //Check validate category name
        return name != null && name.length() <= 20 && name.length() >= 3;
    }
    private boolean isValidDateRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        return startDate.before(endDate) && startDate.after(currentDate) && endDate.after(currentDate);
    }
}
