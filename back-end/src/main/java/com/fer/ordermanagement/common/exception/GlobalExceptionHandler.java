package com.fer.ordermanagement.common.exception;

import com.fer.ordermanagement.common.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            errors.put(err.getField(), err.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                BaseResponse.validationError(errors)
        );
    }

    //404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(
                BaseResponse.error(404, ex.getMessage())
        );
    }

    //409
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<BaseResponse<Void>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(409).body(
                BaseResponse.error(409, ex.getMessage())
        );
    }

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(400).body(
                BaseResponse.error(400, ex.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(
                BaseResponse.error(400, ex.getMessage())
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(400).body(
                BaseResponse.error(400, ex.getMessage())
        );
    }

    // 401 - sai password
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401).body(
                BaseResponse.error(401, "Invalid username or password")
        );
    }

    // 401 - user bị ban/inactive
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<BaseResponse<Void>> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(401).body(
                BaseResponse.error(401, "Account is not active")
        );
    }

    // 403 - không có quyền
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(
                BaseResponse.error(403, "Access denied")
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> handleRuntime(RuntimeException ex) {
        log.error("Runtime exception: ", ex);
        return ResponseEntity.status(500).body(
                BaseResponse.error(500, ex.getMessage())  // trả message thật vì đây là lỗi mình tự throw
        );
    }

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleOther(Exception ex) {
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.status(500).body(
                BaseResponse.error(500, "Internal server error")
        );
    }
}