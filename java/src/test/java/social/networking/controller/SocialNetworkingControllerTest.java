package social.networking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import social.networking.model.Post;
import social.networking.repository.PostRepository;
import social.networking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SocialNetworkingController.class)
class SocialNetworkingControllerTest {

    LocalDateTime localDateTime = LocalDateTime.now();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void get_all_posts_is_successful() throws Exception {
        Post post = new Post("Bob", "Had a good day!");

        post.setTime(localDateTime);

        when(postRepository.findAll()).thenReturn(List.of(post));

        mockMvc.perform(get("/post/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username").value(post.getUsername()))
                .andExpect(jsonPath("$..content").value(post.getContent()));

        verify(postRepository).findAll();
    }

    @Test
    void get_posts_by_username_is_successful() throws Exception {
        Post post = new Post("Steve", "Walked the dog today!");

        post.setTime(localDateTime);

        when(postRepository.findPostsByUsername(post.getUsername())).thenReturn(List.of(post));

        mockMvc.perform(get("/post/Steve")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username").value(post.getUsername()))
                .andExpect(jsonPath("$..content").value(post.getContent()));

        verify(postRepository).findPostsByUsername(post.getUsername());
    }

    @Test
    void create_post_is_successful() throws Exception {
        Post post = new Post("Tom", "Had a big lunch!");

        post.setTime(localDateTime);

        mockMvc.perform(post("/post/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(post)))
                .andExpect(status().isCreated());

        verify(postRepository).save(post);
    }

}