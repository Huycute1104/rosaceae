package com.example.rosaceae.dto.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTOtoCalPercent {
    private int itemId;
    private int shopId;
    // Các trường khác nếu cần
}
