package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Category> getAllUsers() {
        return categoryService.getAllCategory();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<Category> getCategoryByID(@PathVariable int id) {
        return categoryService.getCategoryByID(id);
    }
}
