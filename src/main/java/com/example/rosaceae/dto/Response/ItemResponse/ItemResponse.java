package com.example.rosaceae.dto.Response.ItemResponse;

import com.example.rosaceae.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private String status;
    private Item item;
}
