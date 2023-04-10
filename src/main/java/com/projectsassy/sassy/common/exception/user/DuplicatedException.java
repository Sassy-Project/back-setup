package com.projectsassy.sassy.common.exception.user;

import com.projectsassy.sassy.common.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.common.code.ErrorCode;

public class DuplicatedException extends BusinessExceptionHandler {

    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }

}
