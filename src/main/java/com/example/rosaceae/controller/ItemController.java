package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.ItemDTO;
import com.example.rosaceae.dto.Request.ItemRequest.CreateItemRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Response.ItemResponse.ItemResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/item")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private ItemService itemService;

//  GetByID
    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('shop:read')")
    public Optional<ItemDTO> geItemByID(@PathVariable int id) {
        return itemService.GetItemById(id);
    }

//  GetItem
    @GetMapping("")
//    @PreAuthorize("hasAuthority('shop:read')")
    public Page<ItemDTO> getItems(
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String itemTypeName,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Specification<Item> spec = Specification.where(null);

        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("itemPrice"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.le(root.get("itemPrice"), maxPrice));
        }

        if (categoryName != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("category").get("categoryName"), categoryName));
        }

        if (itemTypeName != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("itemType").get("itemTypeName"), itemTypeName));
        }

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("usersID"), userId));
        }

        Pageable pageable = PageRequest.of(page, size);
        return itemService.getItems(spec, pageable);
    }

//  CreateItem
    @PostMapping("")
//    @PreAuthorize("hasAuthority('shop:create')")
//    public ResponseEntity<ItemResponse> createCategory(@RequestBody ItemRequest request) {
//        return ResponseEntity.ok(itemService.CreateItem(request));
//    }
    public ResponseEntity<ItemResponse> createItemWithImages(
            @RequestParam("itemName") String itemName,
            @RequestParam("quantity") int quantity,
            @RequestParam("itemPrice") Float price,
            @RequestParam("itemDescription") String description,
            @RequestParam("discount") int discount,
            @RequestParam("shopId") int shopId,
            @RequestParam("itemTypeId") int itemTypeId,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("files") List<MultipartFile> files) {

        ItemRequest request = ItemRequest.builder()
                .itemName(itemName)
                .quantity(quantity)
                .itemPrice(price)
                .itemDescription(description)
                .discount(discount)
                .shopId(shopId)
                .itemTypeId(itemTypeId)
                .categoryId(categoryId)
                .files(files)
                .build();

        return ResponseEntity.ok(itemService.createItemWithImages(request));
    }


//  Update Item
    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('shop:update')")
    public ResponseEntity<ItemResponse> updateItemWithImages(
            @PathVariable int id,
            @ModelAttribute CreateItemRequest createItemRequest) {

        return ResponseEntity.ok(itemService.updateItemWithImages(id, createItemRequest));
    }

//  DeleteItem
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('shop:delete')")
    public ResponseEntity<ItemResponse> deleteItem(@PathVariable int id) {
        ItemResponse response = itemService.DeleteItem(id);
        return ResponseEntity.ok(response);
    }
//  GetByUserID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ItemDTO>> getItemsByUserId(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Page<ItemDTO> items = itemService.getItemsByUserId(userId, page, size);
        return ResponseEntity.ok(items);
    }

}
