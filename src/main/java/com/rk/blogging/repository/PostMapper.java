package com.rk.blogging.repository;

import com.rk.blogging.dto.CategoryResponse;
import com.rk.blogging.dto.CommentResponse;
import com.rk.blogging.dto.PostResponse;
import com.rk.blogging.dto.SubCategoryResponse;
import com.rk.blogging.model.Category;
import com.rk.blogging.model.Comment;
import com.rk.blogging.model.Post;
import com.rk.blogging.model.SubCategory;

import java.util.List;

public class PostMapper {

    public static PostResponse toPostResponse(Post post) {

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setUserId(post.getUserId());
        response.setImage(post.getImage());
        response.setImageType(post.getImageType());
        response.setStatus(post.getStatus() != null
                ? post.getStatus().name()
                : null);
        if (post.getSubCategory() != null) {
            SubCategory sc = post.getSubCategory();
            Category c = sc.getCategory();

            response.setSubCategory(
                    new SubCategoryResponse(
                            sc.getId(),
                            sc.getName(),
                            c.getId()
                    )
            );

            response.setCategory(
                    new CategoryResponse(
                            c.getId(),
                            c.getName()
                    )
            );
        }

    /*    List<CommentResponse> comments =
                post.getComments()
                        .stream()
                        .map(PostMapper::toCommentResponse)
                        .toList();*/

   //     response.setComments(comments);

        return response;
    }

    private static CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getCommentText(),
                comment.getCommentedBy(),
                comment.getParentComment() != null
                        ? comment.getParentComment().getId()
                        : null
        );
    }
}
