package com.projectsassy.sassy.exception;

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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalStateExceptionHandler(BusinessExceptionHandler e) {
        log.error("BusinessExceptionHandler", e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)// 미승인 오류
                .body(e.getMessage());
    }
}
