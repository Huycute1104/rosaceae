package com.example.rosaceae.dto.Response.ItemImageResponse;

import com.example.rosaceae.model.ItemImages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImageResponse {
    private String status;
    private List<ItemImages> images;
}
