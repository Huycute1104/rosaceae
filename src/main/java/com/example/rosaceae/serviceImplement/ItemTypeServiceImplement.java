package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.ItemTypeRequest.ItemTypeResponse;
import com.example.rosaceae.dto.Response.CategoryResponse.CategoryResponse;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.model.Category;
import com.example.rosaceae.model.ItemType;
import com.example.rosaceae.repository.ItemTypeRepo;
import com.example.rosaceae.service.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ItemType> findAll(Pageable pageable) {
        return itemTypeRepo.findAll(pageable);
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
        return name != null && name.length() <= 20 && name.length() >= 3;
    }

    @Override
    public ItemTypeResponse updateItemType(ItemTypeRequest request, int ID) {
        //get  name
        String name = request.getName();
        //check exist category
        var ItemType = itemTypeRepo.findByItemTypeId(ID).orElse(null);
        if (ItemType != null) {
            if (isValidName(name)) {
                ItemType.setItemTypeName(name);
                itemTypeRepo.save(ItemType);
                return ItemTypeResponse.builder()
                        .status("Update category successfully")
                        .itemType(ItemType)
                        .build();
            } else {
                return ItemTypeResponse.builder()
                        .status("The ItemType Name must be between 3 and 20 char and should not contain any special characters.")
                        .itemType(null)
                        .build();
            }
        } else {
            return ItemTypeResponse.builder()
                    .status("Category not found")
                    .itemType(null)
                    .build();
        }
    }

    @Override
    public Optional<ItemType> getItemTypeByID(int id) {
        return itemTypeRepo.findByItemTypeId(id);
    }

    @Override
    public ItemTypeResponse deleteItemType(int id) {
        var itemType = itemTypeRepo.findByItemTypeId(id).orElse(null);
        if (itemType == null) {
            return ItemTypeResponse.builder()
                    .status("ItemType not found")
                    .itemType(null)
                    .build();
        }else{
            itemTypeRepo.delete(itemType);
            return ItemTypeResponse.builder()
                    .status("ItemType deleted successfully")
                    .itemType(null)
                    .build();
        }
    }
}
