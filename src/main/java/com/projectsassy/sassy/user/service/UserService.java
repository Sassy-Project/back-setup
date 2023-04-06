package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.DuplicateEmailDto;
import com.projectsassy.sassy.user.dto.DuplicateLoginIdDto;
import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(UserJoinDto joinDto) {

        if (joinDto.getPassword().equals(joinDto.getPasswordCheck())) {
                // 이메일, 아이디 2중 중복검사 여부 확인 필요
            User user = User.builder()
                    .loginId(joinDto.getLoginId())
                    .password(joinDto.getPassword()) // 인코딩 필요
                    .email(joinDto.getEmail())
                    .nickname(joinDto.getNickname())
                    .gender(joinDto.getGender())
                    .mbti(joinDto.getMbti())
                    .image(joinDto.getImage()) // 이미지 인코딩 여부 확인 필요
                    .build();
            userRepository.save(user);
            return user.getUserId();
        }
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    /**
     * 아이디 중복검사
     */
    public void duplicateLoginId(DuplicateLoginIdDto duplicateLoginIdDto) {
        userRepository.findByLoginId(duplicateLoginIdDto.getLoginId())
                .ifPresent(d -> {
                        throw new IllegalArgumentException("중복된 아이디 입니다.");
                });
    }

    /**
     * 이메일 중복검사
     */
    public void duplicateEmail(DuplicateEmailDto duplicateEmailDto) {
        userRepository.findByEmail(duplicateEmailDto.getEmail())
                .ifPresent(d -> {
                    throw new IllegalArgumentException("중복된 이메일 입니다.");
                });
    }
}
