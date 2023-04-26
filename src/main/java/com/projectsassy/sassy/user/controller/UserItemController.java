package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.util.SecurityUtil;
import com.projectsassy.sassy.user.dto.UserAllBadgesResponse;
import com.projectsassy.sassy.user.dto.UserBadgeDto;
import com.projectsassy.sassy.user.service.UserItemService;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserItemController {

    private final UserItemService userItemService;

    @GetMapping("/items/badge")
    public ResponseEntity<UserAllBadgesResponse> findAllBadges() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<UserBadgeDto> userBadgeDtoList = userItemService.findBadges(userId);
        UserAllBadgesResponse userBadges = new UserAllBadgesResponse(userBadgeDtoList);
        return new ResponseEntity<>(userBadges, HttpStatus.OK);
    }
}
