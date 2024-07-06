package com.example.rosaceae.dto.Request.OrderRequest;

import com.example.rosaceae.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatus {
    private OrderStatus status;
}
