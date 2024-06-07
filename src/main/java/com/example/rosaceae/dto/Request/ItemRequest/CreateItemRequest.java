package com.example.rosaceae.dto.Request.ItemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequest {
    private String itemName;
    private int quantity;
    private Float itemPrice;
    private String itemDescription;
    private int discount;
    private int itemTypeId;
    private int categoryId;
    private List<MultipartFile> files;
}
