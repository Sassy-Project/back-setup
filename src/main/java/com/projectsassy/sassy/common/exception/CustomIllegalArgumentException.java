package com.projectsassy.sassy.common.exception;

import com.projectsassy.sassy.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomIllegalArgumentException extends IllegalArgumentException{

    private final ErrorCode errorCode;

    public CustomIllegalArgumentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
