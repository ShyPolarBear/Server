package com.shy_polarbear.server.domain.comment.exception;

import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;

public class CommentException extends CustomException {

    public CommentException(ExceptionStatus exceptionStatus){ super(exceptionStatus);}
}
