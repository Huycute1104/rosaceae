package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("")
//    @PreAuthorize("hasAuthority('admin:read')")
    public Page<Category> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getAllCategory(pageable);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<Category> getCategoryByID(@PathVariable int id) {
        return categoryService.getCategoryByID(id);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<CategoryResponse> updateUser(
            @PathVariable int id,
            @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(request,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteFood(@PathVariable int id) {
        CategoryResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}
