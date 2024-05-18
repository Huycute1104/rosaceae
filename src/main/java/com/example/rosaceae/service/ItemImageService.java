package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Response.ItemImageResponse.ItemImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ItemImageService {
    public ItemImageResponse CreateItemImage(ItemImageRequest request);
    public ItemImageResponse DeleteItemImage(int id);

}