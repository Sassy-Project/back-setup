package com.projectsassy.sassy.exception;

import com.projectsassy.sassy.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomIllegalArgumentException extends IllegalArgumentException{

    private final ErrorCode errorCode;


    public CustomIllegalArgumentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
