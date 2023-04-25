package com.projectsassy.sassy.item.service;

import com.projectsassy.sassy.item.dto.CreateItemRequest;
import com.projectsassy.sassy.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void createItem(CreateItemRequest createItemRequest) {

    }
}
