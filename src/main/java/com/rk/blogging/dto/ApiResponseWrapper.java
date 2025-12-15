package com.rk.blogging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseWrapper<T> {

    private String status;   // SUCCESS / FAILURE
    private int code;
    private String message;
    private T data;
    private Meta meta;
    private ApiError error;
}