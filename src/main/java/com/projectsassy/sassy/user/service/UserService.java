package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.user.domain.Email;
import com.projectsassy.sassy.user.dto.EmailRequest;
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
import java.util.Optional;
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

    //아이디 찾기
    public ResponseFindIdDto findMyId(FindIdDto findIdDto) {
        String redisEmail = redisUtil.getData(findIdDto.getCode());
        String email = findIdDto.getEmail();
        if (!redisEmail.equals(email)){
            throw new BusinessExceptionHandler(ErrorCode.INVALID_TOKEN);
        }
        User findUser = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> {throw new BusinessExceptionHandler(ErrorCode.INVALID_EMAIL);});

        ResponseFindIdDto responseFindIdDto = new ResponseFindIdDto();
        responseFindIdDto.setLoginId(findUser.getLoginId());

        return responseFindIdDto;
    }

    //비밀번호 찾기
    public void findMyPassword(FindPasswordDto findPasswordDto) {
        String redisEmail = redisUtil.getData(findPasswordDto.getCode());
        String email = findPasswordDto.getEmail();
        String loginId = findPasswordDto.getLoginId();

        if (!redisEmail.equals(email)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_TOKEN);
        }
        userRepository.findByEmailAndLoginId(new Email(email), loginId)
                .orElseThrow(() -> {throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_LOGIN_ID);}); //notfound로 변경

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
