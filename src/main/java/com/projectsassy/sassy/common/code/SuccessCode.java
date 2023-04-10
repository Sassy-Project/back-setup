package com.projectsassy.sassy.common.code;

import lombok.Getter;

@Getter
public enum SuccessCode {
    SIGNUP_SUCCESS("SignUp", "회원가입에 성공하였습니다."),
    CAN_USE_ID("CanUseId", "사용 가능한 아이디 입니다."),
    CAN_USE_EMAIL("CanUseEmail", "사용 가능한 이메일 입니다."),
    SEND_NEW_PASSWORD("sendNewPassword", "랜덤 비밀번호가 이메일로 발송되었습니다."),
    SEND_EMAIL("sendEmail", "인증 코드가 이메일로 발송되었습니다.");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
