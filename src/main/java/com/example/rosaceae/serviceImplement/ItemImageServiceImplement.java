package com.example.rosaceae.serviceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Response.ItemImageResponse.ItemImageResponse;
import com.example.rosaceae.model.ItemImages;
import com.example.rosaceae.repository.ItemImageRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.service.ItemImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemImageServiceImplement implements ItemImageService {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemImageRepo itemImageRepo;



    public String uploadImageToCloudinary(MultipartFile file) {
        try {
            // Upload image to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            // Get the URL of the uploaded image from Cloudinary response
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ItemImageResponse CreateItemImage(ItemImageRequest request) {
        List<String> urls = new ArrayList<>();
        List<ItemImages> imagesList = new ArrayList<>();

        var item = itemRepo.findByItemId(request.getItemId()).orElse(null);
        if (item != null) {
            for (MultipartFile file : request.getFiles()) {
                String url = uploadImageToCloudinary(file);
                if (url != null) {
                    ItemImages images = ItemImages.builder()
                            .imageUrl(url)
                            .item(item)
                            .build();
                    imagesList.add(images);
                    urls.add(url);
                }
            }

            if (!imagesList.isEmpty()) {
                // Save all images to the database
                itemImageRepo.saveAll(imagesList);

                return ItemImageResponse.builder()
                        .images(imagesList)
                        .status("Success")
                        .build();
            } else {
                return ItemImageResponse.builder()
                        .images(null)
                        .status("Failed to upload images")
                        .build();
            }
        } else {
            return ItemImageResponse.builder()
                    .images(null)
                    .status("Item Not Found")
                    .build();
        }
    }

    @Override
    public ItemImageResponse DeleteItemImage(int id) {
        return null;
    }
}
