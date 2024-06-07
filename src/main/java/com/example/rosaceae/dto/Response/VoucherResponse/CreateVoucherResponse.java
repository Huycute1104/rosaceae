package com.example.rosaceae.dto.Response.VoucherResponse;

import com.example.rosaceae.model.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoucherResponse {
    private String status;
    private Voucher voucher;
}
