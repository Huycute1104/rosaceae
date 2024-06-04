package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.ItemDTO;
import com.example.rosaceae.dto.Request.ItemRequest.CreateItemRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.Optional;

public interface ItemService {
    public ItemResponse CreateItem(ItemRequest itemRequest);
    Optional<ItemDTO> GetItemById(int id);
    public ItemResponse UpdateItem(CreateItemRequest itemRequest,int id);
    public ItemResponse DeleteItem(int id);
    Page<ItemDTO> getItemsByUserId(int userId, int page, int size);
    public Page<ItemDTO> getItems(Specification<Item> spec, Pageable pageable);

}
