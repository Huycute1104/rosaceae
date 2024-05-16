package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.model.Item;

import java.util.Optional;

public interface ItemService {
    public ItemResponse CreateItem(ItemRequest itemRequest);

    Optional<Item> GetItemById(int id);
}
