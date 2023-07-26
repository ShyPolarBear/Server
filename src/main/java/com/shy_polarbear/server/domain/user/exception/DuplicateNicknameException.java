package com.shy_polarbear.server.domain.user.exception;

import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.Getter;

@Getter
public class DuplicateNicknameException extends UserException{
    private Object data;

    public DuplicateNicknameException(ExceptionStatus exceptionStatus, Object data) {
        super(exceptionStatus);
        this.data = data;
    }
}
