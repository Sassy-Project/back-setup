package com.projectsassy.sassy;

import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final UserRepository userRepository;

        @Transactional
        public void init() {

            User user1 = User.of("qwer1234", "1q2w3e", "qwer@naver.com", "haha", "man", "enfp", "image");
            User user2 = User.of("sassy0401", "123456", "sassy@naver.com", "sassy", "woman", "intp", "image2");

            userRepository.save(user1);
            userRepository.save(user2);
        }

    }

}


