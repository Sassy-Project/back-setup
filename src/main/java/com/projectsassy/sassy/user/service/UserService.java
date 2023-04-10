package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.common.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.projectsassy.sassy.common.code.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


    @Transactional
    public void join(UserJoinDto joinDto) {
        User user = joinDto.toEntity();

        // 패스워드 인코딩
        user.encodingPassword(encoder.encode(joinDto.getPassword()));
        userRepository.save(user);

    }

    //아이디 중복검사
    public void duplicateLoginId(DuplicateLoginIdDto duplicateLoginIdDto) {
        userRepository.findByLoginId(duplicateLoginIdDto.getLoginId())
                .ifPresent(d -> {
                        throw new DuplicatedException(DUPLICATE_LOGIN_ID);
                });
    }

    //이메일 중복검사
    public void duplicateEmail(DuplicateEmailDto duplicateEmailDto) {
        String email = duplicateEmailDto.getEmail();
        userRepository.findByEmail(new Email(email))
            .ifPresent(d -> {
                throw new DuplicatedException(DUPLICATE_EMAIL);
            });
    }

    public User login(LoginRequest loginRequest) {
        User findUser = userRepository.findByLoginId(loginRequest.getLoginId())
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(NOT_REGISTERED_USER);
            });

        if (!encoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new CustomIllegalStateException(WRONG_PASSWORD);
        }

        return findUser;
    }

    public UserProfileResponse getProfile(Long userId) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(NOT_FOUND_USER);
            });

        return new UserProfileResponse(findUser);
    }

    @Transactional
    public UpdateProfileResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(NOT_FOUND_USER);
            });

        String updatedNickname = updateProfileRequest.getNickname();
        String updatedEmail = updateProfileRequest.getEmail();
        String updatedMbti = updateProfileRequest.getMbti();

        findUser.updateProfile(updatedNickname, updatedEmail, updatedMbti);

        return new UpdateProfileResponse(findUser.getNickname());
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(NOT_FOUND_USER);
            });

        if (!encoder.matches(updatePasswordRequest.getPassword(), findUser.getPassword())) {
            throw new CustomIllegalStateException(WRONG_PASSWORD);
        }

        String updatePassword = updatePasswordRequest.getUpdatePassword();
        findUser.changePassword(encoder.encode(updatePassword));
    }
}
