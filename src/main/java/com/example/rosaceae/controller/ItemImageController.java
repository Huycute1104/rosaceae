package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Response.ItemImageResponse.ItemImageResponse;
import com.example.rosaceae.model.ItemImages;
import com.example.rosaceae.service.ItemImageService;
import com.example.rosaceae.service.ItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/itemImages")
@RequiredArgsConstructor
public class ItemImageController {
    @Autowired
    private ItemImageService itemImageService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('shop:create')")
    public ResponseEntity<ItemImageResponse> uploadItemImages(
            @RequestParam("itemId") int itemId,
            @RequestParam("files") List<MultipartFile> files) {

        ItemImageRequest request = ItemImageRequest.builder()
                .itemId(itemId)
                .files(files)
                .build();

        ItemImageResponse response = itemImageService.CreateItemImage(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('shop:delete')")
    public ResponseEntity<ItemImageResponse> deleteItem(@PathVariable int id) {
        ItemImageResponse response = itemImageService.DeleteItemImage(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ItemImages>> getAllItemImages(@PathVariable int id) {
        List<ItemImages> images = itemImageService.getAllItemImages(id);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }


}
