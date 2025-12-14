package com.rk.blogging.utils;

import com.rk.blogging.dto.ApiError;
import com.rk.blogging.dto.ApiResponse;
import com.rk.blogging.dto.Meta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            T data,
            String message,
            HttpStatus status
    ) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status("SUCCESS")
                .code(status.value())
                .message(message)
                .data(data)
                .meta(Meta.builder()
                        .requestId(UUID.randomUUID().toString())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build();

        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Void>> failure(
            ApiError error,
            String message,
            HttpStatus status
    ) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("FAILURE")
                .code(status.value())
                .message(message)
                .error(error)
                .meta(Meta.builder()
                        .requestId(UUID.randomUUID().toString())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build();

        return new ResponseEntity<>(response, status);
    }
}