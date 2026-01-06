package com.rk.blogging.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private byte[] image;
    private String imageType; // image/png, image/jpeg
    private String status;
    private List<CommentResponse> comments;
}
