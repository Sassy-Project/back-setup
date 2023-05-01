package com.projectsassy.sassy.item.controller;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.exception.CustomIllegalArgumentException;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.common.util.SecurityUtil;
import com.projectsassy.sassy.item.dto.AllBadgeResponse;
import com.projectsassy.sassy.item.dto.CreateBadgeRequest;
import com.projectsassy.sassy.item.dto.ItemDeleteRequest;
import com.projectsassy.sassy.item.dto.ItemPurchaseRequest;
import com.projectsassy.sassy.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/badge/new")
    public ResponseEntity createItem(@RequestParam MultipartFile multipartFile, @RequestBody CreateBadgeRequest createBadgeRequest){
        itemService.createBadge(multipartFile, createBadgeRequest, "badge");
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CREATE_ITEM), HttpStatus.OK);
    }

    @GetMapping("/badge")
    public ResponseEntity<AllBadgeResponse> findAllBadge() {
        AllBadgeResponse allBadgeResponse = itemService.findAllBadge();
        return new ResponseEntity<>(allBadgeResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteBadge(@RequestBody ItemDeleteRequest itemDeleteRequest) {
        itemService.deleteItem(itemDeleteRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.DELETE_ITEM), HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseItem(@RequestBody ItemPurchaseRequest itemPurchaseRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        itemService.purchaseItem(userId, itemPurchaseRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.PURCHASE_ITEM), HttpStatus.OK);
    }

}
