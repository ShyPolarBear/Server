package com.shy_polarbear.server.domain.feed.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class FeedException extends CustomException {
    public FeedException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
