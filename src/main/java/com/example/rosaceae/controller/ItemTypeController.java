package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemTypeRequest.ItemTypeResponse;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.ItemType;
import com.example.rosaceae.service.ItemTypeService;
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
@RequestMapping("/api/v1/itemType")
@RequiredArgsConstructor
public class ItemTypeController {
    @Autowired
    private ItemTypeService itemTypeService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ItemTypeResponse> createCategory(@RequestBody ItemTypeRequest request) {
        return ResponseEntity.ok(itemTypeService.createItemType(request));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public Page<ItemType> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return itemTypeService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<ItemType> getCategoryByID(@PathVariable int id) {
        return itemTypeService.getItemTypeByID(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ItemTypeResponse> updateUser(
            @PathVariable int id,
            @RequestBody ItemTypeRequest request) {
        return ResponseEntity.ok(itemTypeService.updateItemType(request,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemTypeResponse> deleteFood(@PathVariable int id) {
        ItemTypeResponse response = itemTypeService.deleteItemType(id);
        return ResponseEntity.ok(response);
    }
}
