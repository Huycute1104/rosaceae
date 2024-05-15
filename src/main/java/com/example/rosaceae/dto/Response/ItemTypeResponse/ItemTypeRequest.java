package com.example.rosaceae.dto.Response.ItemTypeResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemTypeRequest {
    private String name;
}
