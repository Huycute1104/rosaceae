package com.example.rosaceae.serviceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rosaceae.dto.Data.*;
import com.example.rosaceae.dto.Request.ItemRequest.CreateItemRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.ItemImages;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImplement implements ItemService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemTypeRepo itemTypeRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ItemImageRepo itemImageRepo;

    @Override
    public ItemResponse CreateItem(ItemRequest itemRequest) {
        // check ItemType exist
        int itemTypeId = itemRequest.getItemTypeId();
        var itemType = itemTypeRepo.findByItemTypeId(itemTypeId).orElse(null);
        if (itemType == null) {
            return ItemResponse.builder()
                    .item(null)
                    .status("ItemType Not Found")
                    .build();
        }
        //check shop exist
        int userId = itemRequest.getShopId();
        var shop = userRepo.findUserByUsersID(userId).orElse(null);
        if (shop != null && shop.getRole() == Role.SHOP) {
            // check Category exist
            int categoryID = itemRequest.getCategoryId();
            var category = categoryRepo.findCategoriesByCategoryId(categoryID).orElse(null);
            if (category == null) {
                return ItemResponse.builder()
                        .item(null)
                        .status("Category Not Found")
                        .build();
            } else {
                int quantity = 0;
                if (itemTypeId == 2) {
                    quantity = itemRequest.getQuantity();
                }
                Item item = Item.builder()
                        .itemName(itemRequest.getItemName())
                        .itemDescription(itemRequest.getDescription())
                        .itemPrice(itemRequest.getPrice())
                        .quantity(quantity)
                        .itemRate(0f)
                        .commentCount(0)
                        .countUsage(0)
                        .discount(itemRequest.getDiscount())
                        .user(shop)
                        .category(category)
                        .itemType(itemType)
                        .build();
                itemRepo.save(item);
                return ItemResponse.builder()
                        .item(item)
                        .status("Created Item Successfully")
                        .build();

            }

        } else {
            return ItemResponse.builder()
                    .item(null)
                    .status("User Not Found")
                    .build();
        }
    }
private String uploadImageToCloudinary(MultipartFile file) {
    try {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
    @Override
    public ItemResponse createItemWithImages(ItemRequest itemRequest) {
        int itemTypeId = itemRequest.getItemTypeId();
        var itemType = itemTypeRepo.findByItemTypeId(itemTypeId).orElse(null);
        if (itemType == null) {
            return ItemResponse.builder()
                    .item(null)
                    .status("ItemType Not Found")
                    .build();
        }

        int userId = itemRequest.getShopId();
        var shop = userRepo.findUserByUsersID(userId).orElse(null);
        if (shop != null && shop.getRole() == Role.SHOP) {
            int categoryID = itemRequest.getCategoryId();
            var category = categoryRepo.findCategoriesByCategoryId(categoryID).orElse(null);
            if (category == null) {
                return ItemResponse.builder()
                        .item(null)
                        .status("Category Not Found")
                        .build();
            } else {
                int quantity = 0;
                if (itemTypeId == 2) {
                    quantity = itemRequest.getQuantity();
                }
                Item item = Item.builder()
                        .itemName(itemRequest.getItemName())
                        .itemDescription(itemRequest.getDescription())
                        .itemPrice(itemRequest.getPrice())
                        .quantity(quantity)
                        .itemRate(0f)
                        .commentCount(0)
                        .countUsage(0)
                        .discount(itemRequest.getDiscount())
                        .user(shop)
                        .category(category)
                        .itemType(itemType)
                        .build();
                itemRepo.save(item);

                List<MultipartFile> files = itemRequest.getFiles();
                List<ItemImages> imagesList = new ArrayList<>();

                for (MultipartFile file : files) {
                    String url = uploadImageToCloudinary(file);
                    if (url != null) {
                        ItemImages images = ItemImages.builder()
                                .imageUrl(url)
                                .item(item)
                                .build();
                        imagesList.add(images);
                    }
                }

                if (!imagesList.isEmpty()) {
                    itemImageRepo.saveAll(imagesList);
                }

                return ItemResponse.builder()
                        .item(item)
                        .status("Created Item and Images Successfully")
                        .build();
            }
        } else {
            return ItemResponse.builder()
                    .item(null)
                    .status("User Not Found")
                    .build();
        }
    }


    @Override
    public Optional<ItemDTO> GetItemById(int id) {
        return itemRepo.findByItemId(id).map(this::convertToDTO);
    }


    @Override
    public ItemResponse UpdateItem(CreateItemRequest itemRequest, int id) {
        var item = itemRepo.findByItemId(id).orElse(null);
        if (item != null) {
            var itemType = itemTypeRepo.findByItemTypeId(itemRequest.getItemTypeId()).orElse(null);
            if (itemType != null) {
                var category = categoryRepo.findCategoriesByCategoryId(itemRequest.getCategoryId()).orElse(null);
                if (category != null) {
                    item.setItemName(itemRequest.getItemName());
                    item.setItemDescription(itemRequest.getItemDescription());
                    item.setItemPrice(itemRequest.getItemPrice());
                    item.setQuantity(itemRequest.getQuantity());
                    item.setDiscount(itemRequest.getDiscount());
                    item.setCategory(category);
                    item.setItemType(itemType);
                    itemRepo.save(item);
                    return ItemResponse.builder()
                            .item(item)
                            .status("Update Item Successfully")
                            .build();
                }else{
                    return ItemResponse.builder()
                            .item(null)
                            .status("Category Not Found")
                            .build();
                }
            } else {
                return ItemResponse.builder()
                        .item(null)
                        .status("ItemType Not Found")
                        .build();
            }
        } else {
            return ItemResponse.builder()
                    .item(null)
                    .status("Item Not Found")
                    .build();
        }
    }

    @Override
    public ItemResponse DeleteItem(int id) {
        var item = itemRepo.findByItemId(id).orElse(null);
        if (item != null) {
            itemRepo.delete(item);
            return ItemResponse.builder()
                    .item(null)
                    .status("Deleted Item Successfully")
                    .build();
        }else{
            return ItemResponse.builder()
                    .item(null)
                    .status("Item Not Found")
                    .build();
        }
    }

    @Override
    public Page<ItemDTO> getItemsByUserId(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemsPage = itemRepo.findByUserUsersID(userId, pageable);
        return itemsPage.map(this::convertToDTO);
    }

    @Override
    public Page<ItemDTO> getItems(Specification<Item> spec, Pageable pageable)  {
        return itemRepo.findAll(spec, pageable).map(this::convertToDTO);
    }

    private ItemDTO convertToDTO(Item item) {
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDescription(item.getItemDescription())
                .itemRate(item.getItemRate())
                .commentCount(item.getCommentCount())
                .countUsage(item.getCountUsage())
                .quantity(item.getQuantity())
                .discount(item.getDiscount())
                .itemImages(item.getItemImages().stream()
                        .map(image -> ItemImageDTO.builder()
                                .imageUrl(image.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .category(CategoryDTO.builder()
                        .categoryId(item.getCategory().getCategoryId())
                        .categoryName(item.getCategory().getCategoryName())
                        .build())
                .itemType(ItemTypeDTO.builder()
                        .itemTypeId(item.getItemType().getItemTypeId())
                        .itemTypeName(item.getItemType().getItemTypeName())
                        .build())
                .build();
    }

}
