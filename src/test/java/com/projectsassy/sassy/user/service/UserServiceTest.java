package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.DuplicateEmailDto;
import com.projectsassy.sassy.user.dto.DuplicateLoginIdDto;
import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired UserRepository userRepository;

    @Autowired UserService userService;

    @Autowired BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원가입")
    public void save() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("asdf1234", "1q2w3e", "asdf133@naver.com", "haha", "man", "enfp", "image");

        //when
        userService.join(userJoinDto);
        User findUser = userRepository.findByLoginId("asdf1234").orElseThrow();
        String findUserLoginId = findUser.getLoginId();

        //then
        assertThat(findUserLoginId).isEqualTo(userJoinDto.getLoginId());

    }

    @Test
    @DisplayName("아이디 중복검사")
    public void duplicateLoginId() throws Exception {
        DuplicateLoginIdDto duplicateLoginIdDto = new DuplicateLoginIdDto("qwer1234");

        assertThatThrownBy(() -> userService.duplicateLoginId(duplicateLoginIdDto))
            .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("이메일 중복검사")
    public void duplicateEmail() throws Exception {
        DuplicateEmailDto duplicateEmailDto = new DuplicateEmailDto("qwer@naver.com");

        assertThatThrownBy(() -> userService.duplicateEmail(duplicateEmailDto))
            .isInstanceOf(DuplicatedException.class);
    }
}
