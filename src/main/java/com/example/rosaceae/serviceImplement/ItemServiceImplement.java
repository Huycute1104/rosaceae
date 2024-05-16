package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.repository.CategoryRepo;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.ItemTypeRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                if (category.getCategoryId() == 2) {
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

    @Override
    public Optional<Item> GetItemById(int id) {
        return itemRepo.findByItemId(id);
    }
}
