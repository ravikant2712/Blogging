package com.rk.blogging.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiError {
    private String errorCode;
    private String errorMessage;
    private Map<String, String> fieldErrors;
}