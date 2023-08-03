package com.shy_polarbear.server.domain.images.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class ImageException extends CustomException {
    public ImageException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
