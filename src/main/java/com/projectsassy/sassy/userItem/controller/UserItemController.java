package com.projectsassy.sassy.userItem.controller;

import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.common.util.SecurityUtil;
import com.projectsassy.sassy.user.dto.UserAllBadgesResponse;
import com.projectsassy.sassy.user.dto.UserBadgeDto;
import com.projectsassy.sassy.userItem.dto.AttachBadgeRequest;
import com.projectsassy.sassy.userItem.service.UserItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserItemController {

    private final UserItemService userItemService;

    @GetMapping("/items/badge")
    public ResponseEntity<UserAllBadgesResponse> findAllUserBadges() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<UserBadgeDto> userBadgeDtoList = userItemService.findBadges(userId);
        UserAllBadgesResponse userBadges = new UserAllBadgesResponse(userBadgeDtoList);
        return new ResponseEntity<>(userBadges, HttpStatus.OK);
    }

    @PatchMapping("/items/badge")
    public ResponseEntity changeBadgeImage(@RequestBody AttachBadgeRequest attachBadgeRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        userItemService.changeBadgeImage(userId, attachBadgeRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CHANGE_ITEM), HttpStatus.OK);
    }
}
