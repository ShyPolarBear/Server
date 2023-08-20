package com.shy_polarbear.server.global.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private T data;
    private String message;

    private ApiResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, data, "");
    }

    public static ApiResponse error(int code, String message) {
        HashMap<String, String> empty = new HashMap<>();
        return new ApiResponse<>(code, empty, message);
    }

    public static <T> ApiResponse<T> error(int code, T data, String message) {
        return new ApiResponse<>(code, data, message);
    }
}
