package com.shy_polarbear.server.domain.ranking.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class RankingException extends CustomException {
    public RankingException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
