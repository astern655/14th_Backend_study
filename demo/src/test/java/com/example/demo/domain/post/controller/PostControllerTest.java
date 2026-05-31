package com.example.demo.domain.post.controller;

import com.example.demo.domain.post.dto.*;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.service.PostService;
import com.example.demo.global.config.JwtTokenProvider;
import com.example.demo.global.exceprion.CustomException;
import com.example.demo.global.exceprion.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.global.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // Stub jwtTokenProvider to authenticate any request with Authorization header
        lenient().when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        lenient().when(jwtTokenProvider.getUserIdFromToken(anyString())).thenReturn(1L);
    }

    @Test
    @DisplayName("게시글 등록 - 성공")
    void createPost_Success() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("새 게시글", "게시글 내용입니다", "홍길동");
        Post post = Post.builder()
                .postID(1L)
                .title(requestDto.title())
                .content(requestDto.content())
                .author(requestDto.author())
                .build();
        PostCreateResponseDto responseDto = new PostCreateResponseDto(post);

        when(postService.createPost(any(PostCreateRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("새 게시글"))
                .andExpect(jsonPath("$.content").value("게시글 내용입니다"))
                .andExpect(jsonPath("$.author").value("홍길동"));
    }

    @Test
    @DisplayName("게시글 등록 실패 - 제목 누락")
    void createPost_Fail_TitleBlank() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("", "게시글 내용입니다", "홍길동");

        // when & then
        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다"));
    }

    @Test
    @DisplayName("게시글 등록 실패 - 내용 누락")
    void createPost_Fail_ContentBlank() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("새 게시글", "   ", "홍길동");

        // when & then
        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("내용은 필수입니다"));
    }

    @Test
    @DisplayName("게시글 등록 실패 - 작성자 누락")
    void createPost_Fail_AuthorBlank() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("새 게시글", "게시글 내용입니다", null);

        // when & then
        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("작성자는 필수입니다"));
    }

    @Test
    @DisplayName("게시글 목록 조회 - 성공")
    void getAllPosts_Success() throws Exception {
        // given
        Post post1 = Post.builder().postID(1L).title("제목1").content("내용1").author("작성자1").build();
        Post post2 = Post.builder().postID(2L).title("제목2").content("내용2").author("작성자2").build();
        List<PostListResponseDto> list = Arrays.asList(new PostListResponseDto(post2), new PostListResponseDto(post1));

        when(postService.getAllPosts()).thenReturn(list);

        // when & then
        mockMvc.perform(get("/api/posts")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[1].id").value(1L));
    }

    @Test
    @DisplayName("게시글 상세 조회 - 성공")
    void getPost_Success() throws Exception {
        // given
        Post post = Post.builder().postID(1L).title("제목").content("내용").author("작성자").build();
        PostGetResponseDto responseDto = new PostGetResponseDto(post);

        when(postService.getPost(1L)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/posts/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }

    @Test
    @DisplayName("게시글 상세 조회 - 실패 (존재하지 않음)")
    void getPost_NotFound() throws Exception {
        // given
        when(postService.getPost(999L)).thenThrow(new CustomException(ErrorCode.POST_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/posts/999")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다"));
    }

    @Test
    @DisplayName("게시글 수정 - 성공")
    void updatePost_Success() throws Exception {
        // given
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("수정된 제목", "수정된 내용");
        Post post = Post.builder()
                .postID(1L)
                .title(requestDto.title())
                .content(requestDto.content())
                .author("작성자")
                .build();
        PostUpdateResponseDto responseDto = new PostUpdateResponseDto(post);

        when(postService.updatePost(anyLong(), any(PostUpdateRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/posts/1")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    @DisplayName("게시글 수정 - 실패 (수정할 내용 없음)")
    void updatePost_BadRequest() throws Exception {
        // given
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("", "");
        when(postService.updatePost(anyLong(), any(PostUpdateRequestDto.class)))
                .thenThrow(new IllegalArgumentException("수정할 내용이 없습니다"));

        // when & then
        mockMvc.perform(patch("/api/posts/1")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("수정할 내용이 없습니다"));
    }

    @Test
    @DisplayName("게시글 삭제 - 성공")
    void deletePost_Success() throws Exception {
        // given
        PostDeleteResponseDto responseDto = new PostDeleteResponseDto("게시글이 삭제되었습니다.");
        when(postService.deletePost(1L)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(delete("/api/posts/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."));
    }

    @Test
    @DisplayName("게시글 삭제 - 실패 (존재하지 않음)")
    void deletePost_NotFound() throws Exception {
        // given
        when(postService.deletePost(999L)).thenThrow(new CustomException(ErrorCode.POST_NOT_FOUND));

        // when & then
        mockMvc.perform(delete("/api/posts/999")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다"));
    }

    @Test
    @DisplayName("게시글 수정 - 실패 (잘못된 JSON 형식)")
    void updatePost_MalformedJson() throws Exception {
        // when & then
        mockMvc.perform(patch("/api/posts/1")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json-format}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("잘못된 요청 형식입니다"));
    }
}
