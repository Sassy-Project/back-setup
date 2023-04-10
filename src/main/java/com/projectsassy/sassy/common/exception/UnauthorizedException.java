package com.projectsassy.sassy.common.exception;

import com.projectsassy.sassy.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{

    private final ErrorCode errorCode;

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
