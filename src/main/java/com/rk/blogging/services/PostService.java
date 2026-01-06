package com.rk.blogging.services;


import com.rk.blogging.dto.PostResponse;
import com.rk.blogging.exceptions.PostNotFoundException;
import com.rk.blogging.model.Post;
import com.rk.blogging.model.User;
import com.rk.blogging.repository.PostMapper;
import com.rk.blogging.repository.PostRepository;
import com.rk.blogging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(Post post) {
        post.setStatus(Post.Status.DRAFT);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    public List<PostResponse> getAllPostsWithComments() {

        return postRepository.findAllWithComments()
                .stream()
                .map(PostMapper::toPostResponse)
                .toList();
    }

    public Post getPostBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + slug));
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
    }

    public Post updatePost(Long postId, Post post,MultipartFile image)   throws IOException{
        Post updatePost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        try{
            if(post !=null && post.getTitle() !=null){
                updatePost.setTitle( post.getTitle());
                updatePost.setSlug(updatePost.getSlug());
            }
            if(post !=null && post.getContent() !=null){
                updatePost.setContent( post.getContent());
            }
            if(post !=null && post.getStatus() !=null){
                updatePost.setStatus(post.getStatus());
            }
            if (image != null && !image.isEmpty()) {
                updatePost.setImage(image.getBytes());
                updatePost.setImageType(image.getContentType());
            }
        }catch (Exception e)
        {
            throw  new PostNotFoundException("Unable to update the Status, Please try again");
        }
        return postRepository.save(updatePost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Post uploadPostImage(Long postId, MultipartFile file) throws IOException {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setImage(file.getBytes());
        post.setImageType(file.getContentType());

        return postRepository.save(post);
    }

    public byte[] getPostImage(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getImage();
    }

    public String getPostImageType(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getImageType();
    }
}
