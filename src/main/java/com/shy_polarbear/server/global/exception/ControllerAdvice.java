package com.shy_polarbear.server.global.exception;

import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> customExceptionHandler(CustomException ex) {
        int httpCode = ex.exceptionStatus.getHttpCode();
        int code = ex.exceptionStatus.getCustomErrorCode();
        String message = ex.exceptionStatus.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), code, message);
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, message));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        int httpCode = ExceptionStatus.SERVER_ERROR.getHttpCode();
        int code = ExceptionStatus.SERVER_ERROR.getCustomErrorCode();
        String message = ExceptionStatus.SERVER_ERROR.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), code, message);
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, message));
    }
}
