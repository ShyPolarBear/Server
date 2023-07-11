package com.shy_polarbear.server.domain.user.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class AuthException extends CustomException {
    public AuthException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
