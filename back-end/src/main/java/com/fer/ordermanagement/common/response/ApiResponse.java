package com.fer.ordermanagement.common.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final int status;
    private final String message;
    private final T data;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    private ApiResponse(boolean success, int status, String message, T data, Map<String, String> errors) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
    // Success có data
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, 200, "Success", data, null);
    }
    // Success có message
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, 200, message, data, null);
    }
    // Success không có data (update, delete,...)
    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(true, 200, message, null, null);
    }
    // Created (POST)
    public static <T> ApiResponse<T> created(T data){
        return new ApiResponse<>(true, 201, "Created successfully", data, null);
    }
    // Error
    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(false, status, message, null, null);
    }

    public static <T> ApiResponse<T> validationError(Map<String, String> errors) {
        return new ApiResponse<>(false, 400, "Validation failed", null, errors);
    }
}
