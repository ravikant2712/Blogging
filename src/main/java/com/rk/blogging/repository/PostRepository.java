package com.rk.blogging.repository;

import com.rk.blogging.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    Optional<Post> findBySlug(String slug);
    Optional<Post> findById(Long postId);
/*    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN FETCH p.comments
    """)
    List<Post> findAllWithComments();*/

 //   @EntityGraph(attributePaths = "comments")
    Page<Post> findAll(Pageable pageable);

}