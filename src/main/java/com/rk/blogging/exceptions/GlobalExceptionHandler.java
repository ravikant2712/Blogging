package com.rk.blogging.exceptions;

import com.rk.blogging.dto.ApiError;
import com.rk.blogging.dto.ApiResponseWrapper;
import com.rk.blogging.utils.ResponseBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExceptions.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleExpiredJwt(TokenExceptions ex) {

        ApiError error = ApiError.builder()
                .errorCode("AUTH_002")
                .errorMessage("Token expired")
                .build();
        return ResponseBuilder.failure(
                error,
                "Authentication failed",
                HttpStatus.UNAUTHORIZED
        );

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleBadCredentials(
            BadCredentialsException ex
    ) {
        ApiError error = ApiError.builder()
                .errorCode("AUTH_001")
                .errorMessage("Invalid username or password")
                .build();

        return ResponseBuilder.failure(
                error,
                "Authentication failed",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage())
                );

        ApiError apiError = ApiError.builder()
                .errorCode("VALIDATION_003")
                .errorMessage("Invalid input data")
                .fieldErrors(errors)
                .build();

        return ResponseBuilder.failure(
                apiError,
                "Validation failed",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handlePostNotFound(
            PostNotFoundException ex,
            HttpServletRequest request
    ) {

        Map<String, String> errors = new HashMap<>();

        ApiError apiError = ApiError.builder()
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .errorMessage(HttpStatus.NOT_FOUND.name())
                .fieldErrors(errors)
                .build();

        return ResponseBuilder.failure(
                apiError,
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    //    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HandleRuntimeException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleRuntimeException(
            HandleRuntimeException ex,
            HttpServletRequest request
    ) {

        Map<String, String> errors = new HashMap<>();

        ApiError apiError = ApiError.builder()
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .errorMessage(HttpStatus.NOT_FOUND.name())
                .fieldErrors(errors)
                .build();

        return ResponseBuilder.failure(
                apiError,
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        //    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}