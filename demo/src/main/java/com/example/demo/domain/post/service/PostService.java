package com.example.demo.domain.post.service;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.dto.PostCreateRequestDto;
import com.example.demo.domain.post.dto.PostCreateResponseDto;
import com.example.demo.domain.post.dto.PostDeleteResponseDto;
import com.example.demo.domain.post.dto.PostGetResponseDto;
import com.example.demo.domain.post.dto.PostListResponseDto;
import com.example.demo.domain.post.dto.PostUpdateRequestDto;
import com.example.demo.domain.post.dto.PostUpdateResponseDto;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.global.exceprion.CustomException;
import com.example.demo.global.exceprion.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto){
        Post post = Post.create(
                requestDto.title(),
                requestDto.content(),
                requestDto.author()
        );
        post = postRepository.save(post);
        return new PostCreateResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostGetResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return new PostGetResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostUpdateResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        boolean hasTitle = requestDto.title() != null && !requestDto.title().isBlank();
        boolean hasContent = requestDto.content() != null && !requestDto.content().isBlank();

        if (!hasTitle && !hasContent) {
            throw new IllegalArgumentException("수정할 내용이 없습니다");
        }

        post.update(requestDto.title(), requestDto.content());
        return new PostUpdateResponseDto(post);
    }

    @Transactional
    public PostDeleteResponseDto deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);
        return new PostDeleteResponseDto("게시글이 삭제되었습니다.");
    }
}

