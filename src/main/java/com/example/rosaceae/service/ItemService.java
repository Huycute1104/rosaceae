package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;

public interface ItemService {
public ItemResponse CreateItem(ItemRequest itemRequest);
}
