package com.projectsassy.sassy.user.exception;

import com.projectsassy.sassy.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.exception.code.ErrorCode;

public class DuplicateEmailException extends BusinessExceptionHandler {

    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }

}
