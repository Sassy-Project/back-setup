package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.common.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
        UserJoinDto userJoinDto = new UserJoinDto("asdf1234", "1q2w3e", "haha", "asdf133@naver.com",  "man", "enfp", "image");

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

    @Test
    @DisplayName("로그인 성공")
    public void login_success() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("qwer1234", "1q2w3e");

        //when
        User loginUser = userService.login(loginRequest);

        //then
        assertThat(loginUser.getLoginId()).isEqualTo(loginRequest.getLoginId());
    }

    @Test
    @DisplayName("로그인 실패 - id")
    public void login_fail_id() throws Exception {
        LoginRequest loginRequest = new LoginRequest("notRegisterId", "1q2w3e");

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(CustomIllegalStateException.class)
            .hasMessage("등록되지 않은 사용자입니다.");
    }

    @Test
    @DisplayName("로그인 실패 - password")
    public void login_fail_password() throws Exception {
        LoginRequest loginRequest = new LoginRequest("qwer1234", "wrongPassword");

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(CustomIllegalStateException.class)
            .hasMessage("비밀번호가 틀렸습니다.");
    }

    @Test
    @DisplayName("마이페이지 호출 성공")
    public void userProfile_success() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("asdf1234", "1q2w3e", "haha", "asdf133@naver.com",  "man", "enfp", "image");
        userService.join(userJoinDto);

        User findUser = userRepository.findByLoginId("asdf1234").orElseThrow();
        Long id = findUser.getId();

        //when
        UserProfileResponse profile = userService.getProfile(id);

        //then
        assertThat(profile.getLoginId()).isEqualTo(findUser.getLoginId());
        assertThat(profile.getMbti()).isEqualTo(findUser.getMbti());
        assertThat(profile.getNickname()).isEqualTo(findUser.getNickname());
        assertThat(profile.getEmail()).isEqualTo(findUser.getEmail().getEmail());
    }

    @Test
    @DisplayName("마이페이지 호출 실패")
    public void userProfile_fail() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("asdf1234", "1q2w3e", "haha", "asdf133@naver.com",  "man", "enfp", "image");
        userService.join(userJoinDto);

        User findUser = userRepository.findByLoginId("asdf1234").orElseThrow();
        Long id = findUser.getId();

        //when
        Long wrongId = id + 1;

        //then
        assertThatThrownBy(() -> userService.getProfile(wrongId))
            .isInstanceOf(CustomIllegalStateException.class)
            .hasMessage("유저를 찾을 수 없습니다.");
    }
}
