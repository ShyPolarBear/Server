package com.shy_polarbear.server.global.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private Integer code;
    private T data;
    private String message;

    private ApiResponse(T data) {
        this.code = null;
        this.data = data;
    }

    private ApiResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static ApiResponse error(int code, String message) {
        HashMap<String, String> empty = new HashMap<>();
        return new ApiResponse<>(code, empty, message);
    }
}
