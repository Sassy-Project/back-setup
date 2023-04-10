package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.exception.UnauthorizedException;
import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.*;
import com.projectsassy.sassy.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User findUser = userService.login(loginRequest);

        HttpSession session = request.getSession();
        session.setAttribute("userId", findUser.getId());

        return new ResponseEntity<>(new LoginResponse(findUser.getId(), findUser.getNickname()), HttpStatus.OK);
    }

    @ApiOperation(value = "마이페이지 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getProfile(
        @PathVariable(value = ("userId")) Long userId,
        @SessionAttribute(name = "userId", required = false) Long loginUserId
    ) {
        validateUser(userId, loginUserId);

        UserProfileResponse userProfileResponse = userService.getProfile(userId);

        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "마이페이지 수정")
    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
        @PathVariable(value = ("userId")) Long userId,
        @SessionAttribute(name = "userId", required = false) Long loginUserId,
        @Validated @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        validateUser(userId, loginUserId);

        UpdateProfileResponse updateProfileResponse = userService.updateProfile(userId, updateProfileRequest);

        return new ResponseEntity<>(updateProfileResponse, HttpStatus.OK);
    }

    private static void validateUser(Long userId, Long loginUserId) {
        if (loginUserId == null || userId != loginUserId) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

}