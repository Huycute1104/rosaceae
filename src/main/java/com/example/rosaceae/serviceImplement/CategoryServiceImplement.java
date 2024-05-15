package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.repository.CategoryRepo;
import com.example.rosaceae.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryServiceImplement implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        //get category name
        String name = request.getCategoryName();
        //check exist category
        var category = categoryRepo.findCategoriesByCategoryName(name).orElse(null);
        if (category == null) {
            if (isValidName(name)) {
                Category cate = Category.builder()
                        .categoryName(name)
                        .build();
                categoryRepo.save(cate);
                return CategoryResponse.builder()
                        .status("Create new category successfully")
                        .category(cate)
                        .build();
            }else{
                return CategoryResponse.builder()
                        .status("The Category Name must be between 3 and 20 char and should not contain any special characters.")
                        .category(null)
                        .build();
            }

        } else {
            return CategoryResponse.builder()
                    .status("Category is exist")
                    .category(null)
                    .build();
        }
    }

    private boolean isValidName(String name) {
        //Check validate category name
        return name != null && name.length() <= 20 && name.length() >= 3 && !name.matches(".*[^a-zA-Z0-9].*");
    }

    @Override
    public CategoryResponse updateCategory(CreateCategoryRequest request, int ID) {
        return null;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Optional<Category> getCategoryByID(int id) {
        return Optional.empty();
    }

    @Override
    public CategoryResponse deleteCategory(int id) {
        return null;
    }
}
