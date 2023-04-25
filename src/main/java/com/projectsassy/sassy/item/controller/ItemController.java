package com.projectsassy.sassy.item.controller;

import com.projectsassy.sassy.item.dto.CreateItemRequest;
import com.projectsassy.sassy.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

//    @PostMapping("/new")
//    public ResponseEntity createItem(@RequestBody CreateItemRequest createItemRequest) {
//        itemService.createItem(createItemRequest);
//    }
}
