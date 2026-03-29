package com.fer.ordermanagement.common.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class BaseResponse<T> {
    private final boolean success;
    private final int status;
    private final String message;
    private final T data;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    private BaseResponse(boolean success, int status, String message, T data, Map<String, String> errors) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
    // Success có data
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(true, 200, "Success", data, null);
    }
    // Success có message
    public static <T> BaseResponse<T> success(String message, T data){
        return new BaseResponse<>(true, 200, message, data, null);
    }
    // Success không có data (update, delete,...)
    public static <T> BaseResponse<T> success(String message){
        return new BaseResponse<>(true, 200, message, null, null);
    }
    // Created (POST)
    public static <T> BaseResponse<T> created(T data){
        return new BaseResponse<>(true, 201, "Created successfully", data, null);
    }
    // Error
    public static <T> BaseResponse<T> error(int status, String message) {
        return new BaseResponse<>(false, status, message, null, null);
    }

    public static <T> BaseResponse<T> validationError(Map<String, String> errors) {
        return new BaseResponse<>(false, 400, "Validation failed", null, errors);
    }
}
