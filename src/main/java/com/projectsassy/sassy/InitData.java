package com.projectsassy.sassy;

import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final ItemRepository itemRepository;


        public void init() {
            Badge badge1 = new Badge("LV1_Badge", 10, "badge1");
            Badge badge2 = new Badge("LV2_Badge", 20, "badge2");
            Badge badge3 = new Badge("LV3_Badge", 30, "badge3");
            itemRepository.save(badge1);
            itemRepository.save(badge2);
            itemRepository.save(badge3);
        }
    }
}
