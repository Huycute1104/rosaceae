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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
//    @GetMapping("")
//    @PreAuthorize("hasAuthority('admin:read')")
//    public List<Item> getAll() {
//        return itemService.GetAllItems();
//    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public Page<Item> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemService.getAllItems(pageable);
    }
}
