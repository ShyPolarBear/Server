package com.shy_polarbear.server.domain.quiz.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class QuizException extends CustomException {

    public QuizException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }

}

