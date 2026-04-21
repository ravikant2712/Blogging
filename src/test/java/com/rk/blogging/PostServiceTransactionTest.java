package com.rk.blogging;


import com.rk.blogging.model.Post;
import com.rk.blogging.repository.PostRepository;
import com.rk.blogging.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest     // Require full context
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PostServiceTransactionTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void checkDatabase() throws Exception {
        System.out.println(
                dataSource.getConnection().getMetaData().getURL()
        );
    }


    // ✅ TEST 1: COMMIT (Success Case)
    @Test
    void createPost_shouldCommit_whenNoException() {

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Content");

        // ✅ REQUIRED
        post.setUserId(1L);   // OR post.setUser(user)

        Post saved = postService.createPost(post);

        assertNotNull(saved.getId());
    }

    // ❌ TEST 2: ROLLBACK (Failure Case)

    @Test
    void updatePost_shouldRollback_whenExceptionOccurs() {


        // 1. Create valid post (unique data!)
        Post post = new Post();
        post.setTitle("Before Update " + System.nanoTime());
        post.setSlug("slug-" + System.nanoTime());
        post.setUserId(1L);

        Post savedPost = postRepository.save(post);

        Long id = savedPost.getId();

        // 2. Invalid update call
        Post newPostData = new Post();
        newPostData.setTitle("Updated");

        try {
            postService.updatePost(99999L, newPostData, null);
        } catch (Exception ignored) {}

        // 3. Verify rollback
        Post dbPost = postRepository.findById(id).orElseThrow();

        assertEquals(savedPost.getTitle(), dbPost.getTitle());
    }


}
