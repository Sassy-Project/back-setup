package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.user.dto.EmailRequest;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse> signUp(@Validated @RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SIGNUP_SUCCESS));
    }


    @ApiOperation(value = "회원가입 시 아이디 중복 검사")
    @PostMapping("/signUp/id")
    public ResponseEntity<ApiResponse> duplicateLoginId(@Validated @RequestBody DuplicateLoginIdDto duplicateLoginIdDto) {
        userService.duplicateLoginId(duplicateLoginIdDto);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.CAN_USE_ID));
    }


    @ApiOperation(value = "회원가입 시 이메일 중복 검사")
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

        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SEND_NEW_PASSWORD));
    }

    //이메일 전송
    @PostMapping("/email")
    public ResponseEntity authEmail(@Validated @RequestBody EmailRequest request) {
        userService.authEmail(request);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SEND_EMAIL));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        User findUser = userService.login(loginDto);

        HttpSession session = request.getSession();
        session.setAttribute(findUser.getLoginId(), findUser);

        // 로그인할 때 뭐 넘겨줄지.
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.CAN_USE_EMAIL));
    }

}
