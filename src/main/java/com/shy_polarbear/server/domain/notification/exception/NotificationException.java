package com.shy_polarbear.server.domain.notification.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class NotificationException extends CustomException {
    public NotificationException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }

}
