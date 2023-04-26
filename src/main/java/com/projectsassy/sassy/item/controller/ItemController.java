package com.projectsassy.sassy.item.controller;

import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.item.dto.AllBadgeResponse;
import com.projectsassy.sassy.item.dto.CreateBadgeRequest;
import com.projectsassy.sassy.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/badge/new")
    public ResponseEntity createItem(@RequestBody CreateBadgeRequest createBadgeRequest) {
        itemService.createBadge(createBadgeRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CREATE_ITEM), HttpStatus.OK);
    }

    @GetMapping("/badge")
    public ResponseEntity<AllBadgeResponse> findAllBadge() {
        AllBadgeResponse allBadgeResponse = itemService.findAllBadge();
        return new ResponseEntity<>(allBadgeResponse, HttpStatus.OK);
    }
}
