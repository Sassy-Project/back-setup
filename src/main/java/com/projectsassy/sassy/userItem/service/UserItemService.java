package com.projectsassy.sassy.userItem.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.userItem.domain.UserItem;
import com.projectsassy.sassy.user.dto.UserBadgeDto;
import com.projectsassy.sassy.userItem.dto.AttachBadgeRequest;
import com.projectsassy.sassy.userItem.repository.UserItemRepository;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserItemService {

    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;

    public List<UserBadgeDto> findBadges(Long userId) {
        User user = userRepository.findItemsById(userId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
            });

        return user.getUserItems().stream()
            .map(userItem -> new UserBadgeDto((Badge) userItem.getItem()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void changeBadgeImage(Long userId, AttachBadgeRequest attachBadgeRequest) {
        UserItem userItem = userItemRepository.findUserItem(userId, attachBadgeRequest.getItemId())
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USERITEM);
            });

        User user = userItem.getUser();
        Badge badge = (Badge) userItem.getItem();

        user.changeImage(badge.getBadgeImage());
    }
}
