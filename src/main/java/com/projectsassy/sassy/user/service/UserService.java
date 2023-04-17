package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.common.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;


@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder, JavaMailSender javaMailSender, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
    }


    @Transactional
    public void join(UserJoinDto joinDto) {
        User user = joinDto.toEntity();

        user.encodingPassword(encoder.encode(joinDto.getPassword()));
        userRepository.save(user);
    }

    public void duplicateLoginId(DuplicateLoginIdDto duplicateLoginIdDto) {
        userRepository.findByLoginId(duplicateLoginIdDto.getLoginId())
                .ifPresent(d -> {
                        throw new DuplicatedException(ErrorCode.DUPLICATE_LOGIN_ID);
                });
    }

    public void duplicateEmail(DuplicateEmailDto duplicateEmailDto) {
        String email = duplicateEmailDto.getEmail();
        userRepository.findByEmail(new Email(email))
            .ifPresent(d -> {
                throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
            });
    }

    public User login(LoginRequest loginRequest) {
        User findUser = userRepository.findByLoginId(loginRequest.getLoginId())
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_REGISTERED_USER);
            });

        if (!encoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new CustomIllegalStateException(ErrorCode.WRONG_PASSWORD);
        }

        return findUser;
    }

    public UserProfileResponse getProfile(Long userId) {
        User findUser = findById(userId);

        return new UserProfileResponse(findUser.getLoginId(),
            findUser.getNickname(),
            findUser.getEmail().getEmail(),
            findUser.getMbti(),
            findUser.getGender());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
            });
    }

    @Transactional
    public UpdateProfileResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        User findUser = findById(userId);

        String updatedNickname = updateProfileRequest.getNickname();
        String updatedEmail = updateProfileRequest.getEmail();
        String updatedMbti = updateProfileRequest.getMbti();
        String updateGender = updateProfileRequest.getGender();

        findUser.updateProfile(updatedNickname, updatedEmail, updatedMbti, updateGender);

        return new UpdateProfileResponse(updatedNickname, updatedEmail, updatedMbti, updateGender);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        User findUser = findById(userId);

        if (!encoder.matches(updatePasswordRequest.getPassword(), findUser.getPassword())) {
            throw new CustomIllegalStateException(ErrorCode.WRONG_PASSWORD);
        }

        String updatePassword = updatePasswordRequest.getUpdatePassword();
        findUser.changePassword(encoder.encode(updatePassword));
    }

    @Transactional
    public void delete(Long userId) {
        User findUser = findById(userId);
        userRepository.delete(findUser);
    }

    public FindIdResponse findMyId(FindIdRequest findIdRequest) {
        String redisEmail = redisUtil.getData(findIdRequest.getCode());
        String email = findIdRequest.getEmail();
        if (!redisEmail.equals(email)){
            throw new BusinessExceptionHandler(ErrorCode.INVALID_TOKEN);
        }

        User findUser = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> {
                    throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
                });

        FindIdResponse findIdResponse = new FindIdResponse();
        findIdResponse.setLoginId(findUser.getLoginId());

        return findIdResponse;
    }

    public void findMyPassword(FindPasswordRequest findPasswordRequest) {
        String redisEmail = redisUtil.getData(findPasswordRequest.getCode());
        String email = findPasswordRequest.getEmail();
        String loginId = findPasswordRequest.getLoginId();

        if (!redisEmail.equals(email)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_TOKEN);
        }
        userRepository.findByEmailAndLoginId(new Email(email), loginId)
                .orElseThrow(() -> {
                    throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
                });

    }

    //이메일 발송
    @Transactional
    public void authEmail(EmailRequest request) {
        //인증번호 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111); // 범위

        //이메일 발송
        sendAuthEmail(request.getEmail(), authKey);
    }

    private void sendAuthEmail(String email, String authKey) {

        String subject = "MBTI CHAT";
        String text = "MBTI CHAT 인증번호는 " + authKey + "입니다. <br/>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //유효시간
        redisUtil.setDataExpire(authKey, email, 60*5L);

    }
}
