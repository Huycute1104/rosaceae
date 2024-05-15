package com.example.rosaceae.dto.Response.CategoryResponse;

import com.example.rosaceae.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String status;
    private Category category;
}
