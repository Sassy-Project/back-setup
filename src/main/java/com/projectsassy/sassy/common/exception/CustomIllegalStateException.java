package com.projectsassy.sassy.common.exception;

import com.projectsassy.sassy.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomIllegalStateException extends IllegalStateException{

    private final ErrorCode errorCode;

    public CustomIllegalStateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
