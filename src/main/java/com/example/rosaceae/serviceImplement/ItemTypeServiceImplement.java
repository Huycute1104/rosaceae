package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.ItemTypeRequest.ItemTypeResponse;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.ItemType;
import com.example.rosaceae.repository.ItemTypeRepo;
import com.example.rosaceae.service.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemTypeServiceImplement implements ItemTypeService {
    @Autowired
    private ItemTypeRepo itemTypeRepo;


    @Override
    public List<ItemType> findAll() {
        return itemTypeRepo.findAll();
    }

    @Override
    public ItemTypeResponse createItemType(ItemTypeRequest request) {
        String name = request.getName();
        var item = itemTypeRepo.findByItemTypeName(name).orElse(null);
        if (item == null) {
            if (isValidName(name)) {
                ItemType itemType = ItemType.builder()
                        .itemTypeName(name)
                        .build();
                itemTypeRepo.save(itemType);
                return ItemTypeResponse.builder()
                        .itemType(itemType)
                        .status("Create ItemType successful")
                        .build();
            }else {
                return ItemTypeResponse.builder()
                        .status("The ItemType Name must be between 3 and 20 char and should not contain any special characters.")
                        .itemType(null)
                        .build();
            }

        } else {
            return ItemTypeResponse.builder()
                    .itemType(null)
                    .status("ItemType is exist")
                    .build();
        }
    }

    private boolean isValidName(String name) {
        //Check validate
        return name != null && name.length() <= 20 && name.length() >= 3 && !name.matches(".*[^a-zA-Z0-9].*");
    }

    @Override
    public ItemTypeResponse updateItemType(ItemTypeRequest request, int ID) {
        return null;
    }

    @Override
    public Optional<ItemType> getItemTypeByID(int id) {
        return itemTypeRepo.findByItemTypeId(id);
    }

    @Override
    public ItemTypeResponse deleteItemType(int id) {
        return null;
    }
}
