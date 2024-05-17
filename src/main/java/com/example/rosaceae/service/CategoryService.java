package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public CategoryResponse createCategory(CreateCategoryRequest request);
    public CategoryResponse updateCategory(CreateCategoryRequest request , int ID);

    public List<Category> getAllCategory();

    public Optional<Category> getCategoryByID(int id);
    public CategoryResponse deleteCategory(int id);
}
