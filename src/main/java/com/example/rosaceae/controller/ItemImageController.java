package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Response.ItemImageResponse.ItemImageResponse;
import com.example.rosaceae.service.ItemImageService;
import com.example.rosaceae.service.ItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
