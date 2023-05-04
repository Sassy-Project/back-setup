package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.token.dto.*;

import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.common.util.SecurityUtil;
import com.projectsassy.sassy.user.dto.EmailRequest;
import com.projectsassy.sassy.user.dto.*;

import com.projectsassy.sassy.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Validated @RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.SIGNUP_SUCCESS), HttpStatus.OK);
    }


    @ApiOperation(value = "회원가입 시 아이디 중복 검사")
    @PostMapping("/signup/id")
    public ResponseEntity<ApiResponse> duplicateLoginId(@Validated @RequestBody DuplicateLoginIdDto duplicateLoginIdDto) {
        userService.duplicateLoginId(duplicateLoginIdDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CAN_USE_ID), HttpStatus.OK);
    }


    @ApiOperation(value = "회원가입 시 이메일 중복 검사")
    @PostMapping("/signup/email")
    public ResponseEntity<ApiResponse> duplicateEmail(@Validated @RequestBody DuplicateEmailDto duplicateEmailDto) {
        userService.duplicateEmail(duplicateEmailDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CAN_USE_EMAIL), HttpStatus.OK);
    }

    @ApiOperation(value = "아이디 찾기")
    @PostMapping("/find/id")
    public ResponseEntity<FindIdResponse> findId(@Validated @RequestBody FindIdRequest findIdRequest) {
        FindIdResponse findIdResponse = userService.findMyId(findIdRequest);

        return new ResponseEntity<>(findIdResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 찾기")
    @PostMapping("/find/password")
    public ResponseEntity<IdResponse> findPassword(@Validated @RequestBody FindPasswordRequest findPasswordRequest) {
        Long userId = userService.findMyPassword(findPasswordRequest);
        IdResponse idResponse = new IdResponse(userId);

        return new ResponseEntity<>(idResponse, HttpStatus.OK);
    }


    @ApiOperation(value = "인증 코드 이메일 전송")
    @PostMapping("/email")
    public ResponseEntity<ApiResponse> authEmail(@Validated @RequestBody EmailRequest request) {
        userService.authEmail(request);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.SEND_EMAIL), HttpStatus.OK);
    }

    @SneakyThrows
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
        @RequestHeader(value = "Authorization") String acTokenRequest,
        @RequestHeader(value = "RefreshToken") String rfTokenRequest
    ) {
        String accessToken = acTokenRequest.substring(7);
        String refreshToken = rfTokenRequest.substring(7);
        userService.logout(accessToken, refreshToken);

        return new ResponseEntity<>(new ApiResponse(SuccessCode.LOGOUT), HttpStatus.OK);
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
        @RequestHeader(value = "Authorization") String acTokenRequest,
        @RequestHeader(value = "RefreshToken") String rfTokenRequest) {

        String accessToken = acTokenRequest.substring(7);
        String refreshToken = rfTokenRequest.substring(7);

        TokenResponse tokenResponse = userService.reissue(accessToken, refreshToken);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "마이페이지 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable(value = ("userId")) Long userId) {
        Long loginUserId = SecurityUtil.getCurrentUserId();
        validateUserId(userId, loginUserId);

        UserProfileResponse userProfileResponse = userService.getProfile(loginUserId);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    private void validateUserId(Long userId, Long loginUserId) {
        if (userId != loginUserId) {
            throw new CustomIllegalStateException(ErrorCode.NO_MATCHES_INFO);
        }
    }

    @ApiOperation(value = "마이페이지 수정")
    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@PathVariable(value = ("userId")) Long userId,
                                                               @Validated @RequestBody UpdateProfileRequest updateProfileRequest) {
        Long loginUserId = SecurityUtil.getCurrentUserId();
        validateUserId(userId, loginUserId);
        UpdateProfileResponse updateProfileResponse = userService.updateProfile(loginUserId, updateProfileRequest);
        return new ResponseEntity<>(updateProfileResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 수정")
    @PatchMapping("/{userId}/password")
    public ResponseEntity updatePassword(@PathVariable(value = ("userId")) Long userId,
                                         @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Long loginUserId = SecurityUtil.getCurrentUserId();
        validateUserId(userId, loginUserId);
        userService.updatePassword(loginUserId, updatePasswordRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.UPDATE_PASSWORD), HttpStatus.OK);
    }

    @ApiOperation(value = "찾기 이후 비밀번호 변경")
    @PatchMapping("/find/new/password")
    public ResponseEntity<LoginResponse> newPassword(@RequestBody NewPasswordRequest newPasswordRequest) {
        LoginResponse loginResponse = userService.newPassword(newPasswordRequest);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable(value = ("userId")) Long userId) {
        Long loginUserId = SecurityUtil.getCurrentUserId();
        validateUserId(userId, loginUserId);
        userService.delete(loginUserId);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.DELETE_USER), HttpStatus.OK);
    }

}
