package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('shop:read')")
    public Optional<Item> geItemByID(@PathVariable int id) {
        return itemService.GetItemById(id);
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('shop:create')")
    public ResponseEntity<ItemResponse> createCategory(@RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.CreateItem(request));
    }
}
