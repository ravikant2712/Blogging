package com.rk.blogging.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rk.blogging.dto.ApiResponseWrapper;
import com.rk.blogging.dto.LoginRequest;
import com.rk.blogging.dto.PostRequest;
import com.rk.blogging.dto.SlugRequest;
import com.rk.blogging.model.Post;
import com.rk.blogging.model.User;
import com.rk.blogging.services.PostService;
import com.rk.blogging.services.UserService;
import com.rk.blogging.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Blog post CRUD APIs")
public class PostController {

    private final PostService postService;
    private final UserService authService;

    @Operation(
            summary = "Get all posts",
            description = "Returns list of all published posts"
    )
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<Post>>> getAllPosts() {
        List<Post> list = postService.getAllPosts();
        return ResponseBuilder.success(
                list,
                "Post Lists",
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get post by slug",
            description = "Fetch a blog post using SEO-friendly slug"
    )
    @PostMapping("/getPostBySlug")
    public ResponseEntity<ApiResponseWrapper<Post>> getPostBySlug(@RequestBody SlugRequest request) {
        Post post =  postService.getPostBySlug(request.getSlug());
        return ResponseBuilder.success(
                post,
                "Record Found",
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get post by ID",
            description = "Fetch a blog post by ID"
    )
    @PostMapping("/getPostById")
    public ResponseEntity<ApiResponseWrapper<Post>> getPostByID(@RequestBody PostRequest request) {
        Post post =  postService.getPostById(request.getId());
        return ResponseBuilder.success(
                post,
                "Record Found",
                HttpStatus.OK
        );
    }



    @Operation(
            summary = "Create new post",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseWrapper<Post>> createPost(
            @RequestPart("post") String postJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        PostRequest postRequest = mapper.readValue(postJson, PostRequest.class);

        User user = authService.getCurrentUser(); // logged-in user

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .status(postRequest.getStatus())
                .userId(user.getId())
                .build();

        if (image != null && !image.isEmpty()) {
            post.setImage(image.getBytes());
            post.setImageType(image.getContentType());
        }

        Post savedPost = postService.createPost(post);

        return ResponseBuilder.success(
                savedPost,
                "Post Created successfully",
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Update post",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        return postService.updatePost(post);
    }

    @Operation(
            summary = "Delete post",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}