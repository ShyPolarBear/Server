package com.shy_polarbear.server.global.exception;

import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    //validation 에러
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> bindExceptionHandler(BindingResult bindingResult) {
        int code = ExceptionStatus.INVALID_INPUT_VALUE.getCustomErrorCode();
        StringBuilder reason = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String errorMessage = fieldError.getField() + " : " + fieldError.getDefaultMessage();
            reason.append(errorMessage).append(", ");
        }
        log.warn("ValidationException({}) - {}", code, reason);
        return ApiResponse.error(code, String.valueOf(reason));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> customExceptionHandler(CustomException ex) {
        int httpCode = ex.exceptionStatus.getHttpCode();
        int code = ex.exceptionStatus.getCustomErrorCode();
        String message = ex.exceptionStatus.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), code, message);
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, message));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ApiResponse> duplicateExceptionHandler(DuplicateNicknameException ex) {
        int httpCode = ex.exceptionStatus.getHttpCode();
        int code = ex.exceptionStatus.getCustomErrorCode();
        String message = ex.exceptionStatus.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), code, message);
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, ex.getData(), message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(Exception ex) {
        int httpCode = ExceptionStatus.SERVER_ERROR.getHttpCode();
        int code = ExceptionStatus.SERVER_ERROR.getCustomErrorCode();
        String message = ExceptionStatus.SERVER_ERROR.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), code, ex.getMessage());
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, message));
    }
}
