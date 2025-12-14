package com.rk.blogging.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Meta {
    private String requestId;
    private LocalDateTime timestamp;
}
