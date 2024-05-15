package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemTypeRequest.ItemTypeResponse;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.service.ItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
