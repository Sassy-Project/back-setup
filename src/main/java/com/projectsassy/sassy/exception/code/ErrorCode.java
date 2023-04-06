package com.projectsassy.sassy.exception.code;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //user
    DUPLICATE_EMAIL(400, "U001", "중복된 이메일입니다.");

    //에러의 코드 상태 반환
    private final int status;

    //에러 코드의 코드 간의 구분 값 ex) user 첫번째 > U001
    private final String divisionCode;

    // 에러 코드의 메시지
    private final String message;

    ErrorCode(int status, String divisionCode, String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
