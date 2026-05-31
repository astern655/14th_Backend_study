package com.example.demo.domain.post.service;

import com.example.demo.domain.post.dto.*;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.global.exceprion.CustomException;
import com.example.demo.global.exceprion.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 등록 성공")
    void createPost_Success() {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목", "내용", "작성자");
        Post post = Post.builder()
                .postID(1L)
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        PostCreateResponseDto responseDto = postService.createPost(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.id());
        assertEquals("제목", responseDto.title());
        assertEquals("내용", responseDto.content());
        assertEquals("작성자", responseDto.author());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void getPost_Success() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .postID(postId)
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostGetResponseDto responseDto = postService.getPost(postId);

        // then
        assertNotNull(responseDto);
        assertEquals(postId, responseDto.id());
        assertEquals("제목", responseDto.title());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 상세 조회 실패 - 존재하지 않는 게시글")
    void getPost_Fail_NotFound() {
        // given
        Long postId = 999L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.getPost(postId);
        });
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getAllPosts_Success() {
        // given
        Post post1 = Post.builder().postID(1L).title("제목1").content("내용1").author("작성자1").build();
        Post post2 = Post.builder().postID(2L).title("제목2").content("내용2").author("작성자2").build();
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(post2, post1));

        // when
        List<PostListResponseDto> posts = postService.getAllPosts();

        // then
        assertEquals(2, posts.size());
        assertEquals(2L, posts.get(0).id());
        assertEquals(1L, posts.get(1).id());
        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_Success() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .postID(postId)
                .title("원래 제목")
                .content("원래 내용")
                .author("작성자")
                .build();
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("수정된 제목", "수정된 내용");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostUpdateResponseDto responseDto = postService.updatePost(postId, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("수정된 제목", post.getTitle());
        assertEquals("수정된 내용", post.getContent());
        assertEquals("수정된 제목", responseDto.title());
        assertEquals("수정된 내용", responseDto.content());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 존재하지 않는 게시글")
    void updatePost_Fail_NotFound() {
        // given
        Long postId = 999L;
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("수정된 제목", "수정된 내용");
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.updatePost(postId, requestDto);
        });
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 수정할 내용이 없음")
    void updatePost_Fail_NoChanges() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .postID(postId)
                .title("원래 제목")
                .content("원래 내용")
                .author("작성자")
                .build();
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(null, "   ");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(postId, requestDto);
        });
        assertEquals("수정할 내용이 없습니다", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deletePost_Success() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .postID(postId)
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostDeleteResponseDto responseDto = postService.deletePost(postId);

        // then
        assertNotNull(responseDto);
        assertEquals("게시글이 삭제되었습니다.", responseDto.message());
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 게시글")
    void deletePost_Fail_NotFound() {
        // given
        Long postId = 999L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.deletePost(postId);
        });
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }
}
