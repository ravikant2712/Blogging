package com.rk.blogging.services;

import com.rk.blogging.dto.CommentRequest;
import com.rk.blogging.dto.ReplyCommentRequest;
import com.rk.blogging.exceptions.HandleRuntimeException;
import com.rk.blogging.model.Comment;
import com.rk.blogging.model.Post;
import com.rk.blogging.model.User;
import com.rk.blogging.repository.CommentRepository;
import com.rk.blogging.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment createComment(CommentRequest request, User user) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCommentText(request.getCommentText());
        comment.setCommentedBy(user.getUsername()); // or user.getEmail()

        return commentRepository.save(comment);
    }


    public Comment replyToComment(ReplyCommentRequest request, User user) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new HandleRuntimeException("Post not found"));

        Comment parentComment = commentRepository.findById(request.getParentCommentId())
                .orElseThrow(() -> new HandleRuntimeException("Parent comment not found"));

        Comment reply = new Comment();
        reply.setPost(post);
        reply.setParentComment(parentComment);
        reply.setCommentText(request.getCommentText());
        reply.setCommentedBy(user.getUsername());

        return commentRepository.save(reply);
    }


}

