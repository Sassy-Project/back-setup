package com.projectsassy.sassy.common.exception;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    //비즈니스 로직 exception
    @ExceptionHandler(BusinessExceptionHandler.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessExceptionHandler e) {
        log.error("BusinessExceptionHandler", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.from(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);

        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, extractErrorReason(e));

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(CustomIllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleCustomIllegalArgumentException(CustomIllegalArgumentException e) {
        log.error("CustomIllegalArgumentException", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.from(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(CustomIllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handleCustomIllegalStateException(CustomIllegalStateException e) {
        log.error("CustomIllegalStateException", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.from(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.from(errorCode);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    private static String extractErrorReason(MethodArgumentNotValidException e) {
        return e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList())
            .get(0);
    }
    
}
