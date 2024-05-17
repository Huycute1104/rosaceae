package com.example.rosaceae.dto.Request.ItemTypeRequest;

import com.example.rosaceae.model.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemTypeResponse {
    private ItemType itemType;
    private String status;
}
