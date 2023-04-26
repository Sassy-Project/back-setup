package com.projectsassy.sassy.item.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.item.domain.Item;
import com.projectsassy.sassy.item.dto.AllBadgeResponse;
import com.projectsassy.sassy.item.dto.BadgeDto;
import com.projectsassy.sassy.item.dto.CreateBadgeRequest;
import com.projectsassy.sassy.item.dto.ItemDeleteRequest;
import com.projectsassy.sassy.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void createBadge(CreateBadgeRequest createBadgeRequest) {
        Badge badge = new Badge(createBadgeRequest.getItemName(),
            createBadgeRequest.getPrice(),
            createBadgeRequest.getBadgeImage());
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
        Item findItem = itemRepository.findById(itemDeleteRequest.getItemId())
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_ITEM);
            });
        itemRepository.delete(findItem);
    }
}
