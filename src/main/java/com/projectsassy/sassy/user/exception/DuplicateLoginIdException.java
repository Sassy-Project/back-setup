package com.projectsassy.sassy.user.exception;

import com.projectsassy.sassy.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.exception.code.ErrorCode;

public class DuplicateLoginIdException extends BusinessExceptionHandler {

    public DuplicateLoginIdException(ErrorCode errorCode) {
        super(errorCode);
    }

}
