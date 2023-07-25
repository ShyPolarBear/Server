package com.shy_polarbear.server.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    protected ExceptionStatus exceptionStatus;

    public CustomException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
