package com.projectsassy.sassy.item.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.item.domain.Item;
import com.projectsassy.sassy.s3.S3Service;
import com.projectsassy.sassy.userItem.domain.UserItem;
import com.projectsassy.sassy.item.dto.*;
import com.projectsassy.sassy.item.repository.ItemRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final S3Service s3Service;

    @Transactional
    public void createBadge(MultipartFile multipartFile, CreateBadgeRequest createBadgeRequest, String dirName) {
        String imagePath = s3Service.upload(multipartFile, dirName);
        Badge badge = new Badge(createBadgeRequest.getItemName(),
            createBadgeRequest.getPrice(),
            imagePath);
        itemRepository.save(badge);
    }

    public AllBadgeResponse findAllBadge() {
        List<Item> badge = itemRepository.findByDtype("Badge");
        List<BadgeDto> badgeDtos = badge.stream()
            .map(b -> new BadgeDto((Badge) b))
            .collect(Collectors.toList());
        return new AllBadgeResponse(badgeDtos);
    }

    @Transactional
    public void deleteItem(ItemDeleteRequest itemDeleteRequest) {
        Item findItem = findItemById(itemDeleteRequest.getItemId());
        itemRepository.delete(findItem);
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_ITEM);
            });
    }

    @Transactional
    public void purchaseItem(Long userId, ItemPurchaseRequest itemPurchaseRequest) {
        User user = userService.findById(userId);
        Item item = findItemById(itemPurchaseRequest.getItemId());
        UserItem userItem = UserItem.createUserItem(item);

        user.purchaseItem(userItem);
    }
}
