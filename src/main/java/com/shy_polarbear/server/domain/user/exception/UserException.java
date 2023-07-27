package com.shy_polarbear.server.domain.user.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class UserException extends CustomException {

    public UserException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }

}
