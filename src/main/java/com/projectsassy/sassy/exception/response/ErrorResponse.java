package com.projectsassy.sassy.exception.response;

import com.projectsassy.sassy.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String divisionCode;
    private String message;

    private ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.divisionCode = errorCode.getDivisionCode();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }
}
