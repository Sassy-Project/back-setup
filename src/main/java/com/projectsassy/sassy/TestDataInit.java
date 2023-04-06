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
            User user1 = User.builder()
                .loginId("qwer1234")
                .password("1q2w3e")
                .email("qwer@naver.com")
                .nickname("haha")
                .gender("man")
                .mbti("enfp")
                .image("image")
                .build();

            User user2 = User.builder()
                .loginId("sassy0401")
                .password("123456")
                .email("sassy@naver.com")
                .nickname("sassy")
                .gender("woman")
                .mbti("intp")
                .image("image2")
                .build();

            userRepository.save(user1);
            userRepository.save(user2);
        }

    }

}


