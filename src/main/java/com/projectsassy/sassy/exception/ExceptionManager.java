package com.projectsassy.sassy.exception;

import com.projectsassy.sassy.exception.code.ErrorCode;
import com.projectsassy.sassy.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    /**
     * 비즈니스 오류
     */
    @ExceptionHandler(BusinessExceptionHandler.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessExceptionHandler e) {
        log.error("BusinessExceptionHandler", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
