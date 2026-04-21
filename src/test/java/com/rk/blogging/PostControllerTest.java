package com.rk.blogging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rk.blogging.configuration.JwtAuthenticationFilter;
import com.rk.blogging.controller.PostController;
import com.rk.blogging.dto.PostRequest;
import com.rk.blogging.dto.SlugRequest;
import com.rk.blogging.model.Post;
import com.rk.blogging.model.User;
import com.rk.blogging.services.CommentService;
import com.rk.blogging.services.PostService;
import com.rk.blogging.services.SubCategoryService;
import com.rk.blogging.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(
        controllers = PostController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)    // loads only controller layer

// @AutoConfigureMockMvc → sets up MockMvc
//Lets you test APIs without running server
//Works with Spring Security (by default)
//Used mainly with @SpringBootTest
//Optional with @WebMvcTest
@AutoConfigureMockMvc    // Loads FULL application    ✔ Real services + DB (if configured)
public class PostControllerTest {

    @Autowired   // inject dependency
    private MockMvc mockMvc;    //This is fake http client Created by  @WebMvcTest or @SpringBootTest or @AutoConfigureMockMvc

    @MockBean    //Create a FAKE version of a Spring bean and put it in the context    //Don’t use the real PostService, use a mock instead.
    private PostService postService;
    @MockBean
    private UserService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private SubCategoryService subCategoryService;

    // ---------- PUBLIC APIs ----------

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void getAllPosts_shouldReturnList() throws Exception {

        when(postService.getAllPosts()).thenReturn(List.of(
                Post.builder().id(1L).title("Post 1").build()
        ));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
         //       .andExpect(jsonPath("$.data[0].title").value("Post 1"));
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void getPostBySlug_shouldReturnPost() throws Exception {
        SlugRequest request = new SlugRequest();
        request.setSlug("this-is-a-second-post");

        when(postService.getPostBySlug("this-is-a-second-post"))
                .thenReturn(Post.builder().id(1L).title("Spring Boot").build());

        mockMvc.perform(post("/api/posts/getPostBySlug")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200));
//                .andExpect(jsonPath("$.data.id").value(1));
            //    .andExpect(jsonPath("$.data.title").value("Spring Boot"));
    }

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void getPostById_shouldReturnPost() throws Exception {

        PostRequest request = new PostRequest();
        request.setId(1L);

        when(postService.getPostById(1L))
                .thenReturn(Post.builder().id(1L).title("By ID").build());

        mockMvc.perform(post("/api/posts/getPostById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // ---------- AUTHENTICATED APIs ----------

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void createPost_withAuth_shouldCreatePost() throws Exception {

        User user = User.builder().id(1L).username("ravikant2712").build();
        when(authService.getCurrentUser()).thenReturn(user);

        when(postService.createPost(any(Post.class)))
                .thenReturn(Post.builder().id(1L).title("Created").build());

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Created");
        postRequest.setContent("Content");

        MockMultipartFile postJson =
                new MockMultipartFile(
                        "post",
                        "",
                        "application/json",
                        objectMapper.writeValueAsBytes(postRequest)
                );

        mockMvc.perform(multipart("/api/posts")
                        .file(postJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
            //    .andExpect(jsonPath("$.data.title").value("Created"));
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void updatePost_withAuth_shouldUpdatePost() throws Exception {

        Post post = Post.builder().id(1L).title("Updated").build();

        when(postService.updatePost(anyLong(), any(Post.class), any()))
                .thenReturn(post);

        MockMultipartFile postJson =
                new MockMultipartFile(
                        "post",
                        "",
                        "application/json",
                        objectMapper.writeValueAsBytes(post)
                );

        mockMvc.perform(multipart("/api/posts/updatePost")
                        .file(postJson)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }))
                .andExpect(status().isOk())
         //       .andExpect(jsonPath("$.data.title").value("Updated"));
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockUser(username = "ravikant2712@gmail.com", roles = "USER")
    void deletePost_withAuth_shouldDelete() throws Exception {

        doNothing().when(postService).deletePost(1L);

        mockMvc.perform(delete("/api/posts/{id}", 1).with(csrf()))
                .andExpect(status().isOk());
    }

    // ---------- UNAUTHORIZED TEST ----------

    @Test
    void createPost_withoutAuth_shouldFail() throws Exception {

        MockMultipartFile postJson =
                new MockMultipartFile(
                        "post",
                        "",
                        "application/json",
                        "{}".getBytes()
                );

        mockMvc.perform(multipart("/api/posts")
                        .file(postJson).with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
