package com.example.rosaceae.dto.Request.VoucherRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoucherRequest {
    private String voucherName;
    private Date startDate;
    private Date endDate;
    private int value;
    private int userid = 2;
}
