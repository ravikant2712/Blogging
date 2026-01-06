package com.rk.blogging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyCommentRequest {

    @NotNull
    private Long postId;

    @NotNull
    private Long parentCommentId;

    @NotBlank
    private String commentText;

    // getters & setters
}