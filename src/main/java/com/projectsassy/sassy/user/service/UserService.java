package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.common.util.RedisUtil;
import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.item.domain.Item;
import com.projectsassy.sassy.item.repository.ItemRepository;
import com.projectsassy.sassy.token.TokenProvider;
import com.projectsassy.sassy.token.dto.TokenResponse;
import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.common.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final TokenProvider tokenProvider;
    private final ItemRepository itemRepository;


    public UserService(AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository,
                       BCryptPasswordEncoder encoder, JavaMailSender javaMailSender, RedisUtil redisUtil, TokenProvider tokenProvider,
                       ItemRepository itemRepository
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
        this.tokenProvider = tokenProvider;
        this.itemRepository = itemRepository;
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

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        User user = findById(Long.valueOf(authenticate.getName()));
        user.addPoint();
        TokenResponse tokenResponse = tokenProvider.generateTokenDto(authenticate);
        redisUtil.setDataExpire(authenticate.getName(), tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7);

        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getNickname(), tokenResponse);
        return loginResponse;
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
        if (!redisEmail.equals(email)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_NUMBER);
        }

        User findUser = userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
            });

        FindIdResponse findIdResponse = new FindIdResponse();
        findIdResponse.setLoginId(findUser.getLoginId());

        return findIdResponse;
    }

    public Long findMyPassword(FindPasswordRequest findPasswordRequest) {
        String redisEmail = redisUtil.getData(findPasswordRequest.getCode());
        String email = findPasswordRequest.getEmail();
        String loginId = findPasswordRequest.getLoginId();

        if (!redisEmail.equals(email)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_NUMBER);
        }

        User findUser = userRepository.findByEmailAndLoginId(new Email(email), loginId)
                .orElseThrow(() -> {
                    throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
                });

        return findUser.getId();
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
        redisUtil.setDataExpire(authKey, email, 60 * 5L);

    }

    public TokenResponse reissue(String accessToken, String refreshToken) {
        // 1. 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomIllegalStateException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Access Token 에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userId = authentication.getName();

        // 3. 저장소에서 User ID 를 기반으로 Refresh Token 값 가져오기
        String findRefreshToken = redisUtil.getData(userId);
        if (findRefreshToken == null) {
            throw new CustomIllegalStateException(ErrorCode.INVALID_TOKEN);
        }

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.equals(findRefreshToken)) {
            throw new CustomIllegalStateException(ErrorCode.NO_MATCHES_INFO);
        }

        // 5. 새로운 토큰 생성
        TokenResponse tokenResponse = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        redisUtil.setDataExpire(userId, tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7);

        return tokenResponse;
    }

    public void logout(String accessToken, String refreshToken) {
        // 1. 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomIllegalStateException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Access Token 에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userId = authentication.getName();

        // 3. Redis 에서 해당 ID 로 저장된 Refresh Token 이 있는지 여부 확인 후 삭제
        String findRefreshToken = redisUtil.getData(userId);
        if (findRefreshToken != null) {
            redisUtil.deleteData(userId);
        }
        // 남은 유효시간
        Long expiration = tokenProvider.getExpiration(accessToken);

        // 4. 해당 Access Token 저장
        redisUtil.setDataExpire(accessToken, "logout", expiration);
    }

    //비밀번호 찾기 이후 새 비밀번호
    @Transactional
    public LoginResponse newPassword(NewPasswordRequest newPasswordRequest) {

        if (!newPasswordRequest.getNewPassword().equals(newPasswordRequest.getNewPasswordCheck())) {
            throw new CustomIllegalStateException(ErrorCode.NO_MATCHES_PASSWORD);
        }
        User findUser = findById(newPasswordRequest.getUserId());
        findUser.changePassword(encoder.encode(newPasswordRequest.getNewPassword()));


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(findUser.getLoginId(), newPasswordRequest.getNewPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenResponse tokenResponse = tokenProvider.generateTokenDto(authenticate);

        redisUtil.setDataExpire(authenticate.getName(), tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7);

        LoginResponse loginResponse = new LoginResponse(findUser.getId(), findUser.getNickname(), tokenResponse);

        return loginResponse;
    }


}
