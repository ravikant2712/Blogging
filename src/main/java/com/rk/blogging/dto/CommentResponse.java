package com.rk.blogging.dto;

import com.rk.blogging.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String commentText;
    private String commentedBy;
    private Long parentCommentId;

}