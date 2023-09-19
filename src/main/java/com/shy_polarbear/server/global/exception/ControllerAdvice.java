package com.shy_polarbear.server.global.exception;

import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


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
        ex.printStackTrace();
        return ResponseEntity.status(httpCode)
                .body(ApiResponse.error(code, message));
    }

    /**
     * Client Bad Request
     **/
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

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpClientErrorException(HttpClientErrorException ex) {
        int httpCode = ExceptionStatus.CLIENT_ERROR.getHttpCode();
        String message = ex.getMessage();

        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), httpCode, message);
        return ApiResponse.error(ExceptionStatus.CLIENT_ERROR.getCustomErrorCode(), message);
    }

    @ExceptionHandler(ServletRequestBindingException.class)// @RequestParam 누락
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServletRequestBindingException(ServletRequestBindingException ex) {
        log.warn("{} - {}", ex.getClass().getName(), ex.getMessage());
        return ApiResponse.error(ExceptionStatus.CLIENT_ERROR.getCustomErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)// 잘못된 요청 body
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("{} - {}", ex.getClass().getName(), ex.getMessage());
        return ApiResponse.error(ExceptionStatus.CLIENT_ERROR.getCustomErrorCode(), ex.getLocalizedMessage());
    }
}
