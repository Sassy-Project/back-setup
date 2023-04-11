package com.projectsassy.sassy.common.exception;

import com.projectsassy.sassy.common.code.ErrorCode;
import lombok.Getter;

/**
 * 에러 사용을 위한 구현체
 * 비즈니스 로직 내에서 발생하는 exception 처리
 */
@Getter
public class BusinessExceptionHandler extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
