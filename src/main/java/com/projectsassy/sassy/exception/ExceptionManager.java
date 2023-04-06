package com.projectsassy.sassy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    /**
     * 미승인 오류 (중복확인)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> IllegalStateExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)// 미승인 오류
                .body(e.getMessage());
    }
}
