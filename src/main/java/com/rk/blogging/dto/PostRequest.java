package com.rk.blogging.dto;

import com.rk.blogging.model.Post;
import lombok.Data;

@Data
public class PostRequest {
    Long id;
    private String title;
    private String content;
    private Post.Status status;
}