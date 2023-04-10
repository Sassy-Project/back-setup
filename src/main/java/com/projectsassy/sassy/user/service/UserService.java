package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.DuplicateEmailDto;
import com.projectsassy.sassy.user.dto.DuplicateLoginIdDto;
import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.common.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        throw new DuplicatedException(ErrorCode.DUPLICATE_LOGIN_ID);
                });
    }

    //이메일 중복검사
    public void duplicateEmail(DuplicateEmailDto duplicateEmailDto) {
        String email = duplicateEmailDto.getEmail();
        userRepository.findByEmail(new Email(email))
            .ifPresent(d -> {
                throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
            });
    }
}
