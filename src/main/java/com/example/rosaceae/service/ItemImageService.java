package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.DummyDataIImages;
import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Response.ItemImageResponse.ItemImageResponse;
import com.example.rosaceae.model.ItemImages;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemImageService {
    public ItemImageResponse CreateItemImage(ItemImageRequest request);
    public ItemImageResponse DeleteItemImage(int id);
    List<ItemImages> getAllItemImages(int id);
    public ItemImageResponse CreateImage(DummyDataIImages images);

}