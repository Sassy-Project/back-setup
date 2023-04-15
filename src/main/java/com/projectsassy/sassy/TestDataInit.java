package com.projectsassy.sassy;

import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        private final BCryptPasswordEncoder encoder;

        @Transactional
        public void init() {

            User user1 = User.of("qwer1234", encoder.encode("1q2w3e"), "haha", "qwer@naver.com", "man", "enfp", "image");
            User user2 = User.of("sassy0401", encoder.encode("123456"), "sassy", "sassy@naver.com",  "woman", "intp", "image2");
            User user3 = User.of("ghdb132", encoder.encode("123456"), "hobin", "ghdb132@naver.com",  "woman", "istp", "image3");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        }

    }

}


