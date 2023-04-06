package com.projectsassy.sassy.exception.user;

import com.projectsassy.sassy.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.exception.code.ErrorCode;

public class DuplicatedException extends BusinessExceptionHandler {

    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }

}
