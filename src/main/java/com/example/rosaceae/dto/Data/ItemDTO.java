package com.example.rosaceae.dto.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private int itemId;
    private String itemName;
    private Float itemPrice;
    private String itemDescription;
    private Float itemRate;
    private int commentCount;
    private int countUsage;
    private int quantity;
    private int discount;
    private List<ItemImageDTO> itemImages;
    private CategoryDTO category;
    private ItemTypeDTO itemType;
}
