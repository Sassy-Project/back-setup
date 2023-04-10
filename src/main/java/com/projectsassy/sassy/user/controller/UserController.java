package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.user.dto.EmailRequest;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse> signUp(@Validated @RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SIGNUP_SUCCESS));
    }


    //아이디 중복검사
    @PostMapping("/signUp/id")
    public ResponseEntity<ApiResponse> duplicateLoginId(@Validated @RequestBody DuplicateLoginIdDto duplicateLoginIdDto) {
        userService.duplicateLoginId(duplicateLoginIdDto);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.CAN_USE_ID));
    }


    //이메일 중복검사
    @PostMapping("/signUp/email")
    public ResponseEntity<ApiResponse> duplicateEmail(@Validated @RequestBody DuplicateEmailDto duplicateEmailDto) {
        userService.duplicateEmail(duplicateEmailDto);

        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.CAN_USE_EMAIL));
    }

    //아이디 찾기
    @PostMapping("/find/id")
    public ResponseEntity findMyId(@Validated @RequestBody FindIdDto findIdDto) {
        ResponseFindIdDto myId = userService.findMyId(findIdDto);

        return ResponseEntity.ok().body(myId);
    }

    //비밀번호 찾기
    @PostMapping("/find/password")
    public ResponseEntity findPassword(@Validated @RequestBody FindPasswordDto findPasswordDto) {
        userService.findMyPassword(findPasswordDto);

        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.CERTIFI_CODE));
    }

    //이메일 전송
    @PostMapping("/email")
    public ResponseEntity authEmail(@Validated @RequestBody EmailRequest request) {
        userService.authEmail(request);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SEND_EMAIL));
    }
}
