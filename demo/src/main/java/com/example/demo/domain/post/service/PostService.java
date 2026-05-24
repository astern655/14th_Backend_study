package com.example.demo.domain.post.service;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.dto.PostRequestDto;
import com.example.demo.domain.post.dto.PostResponseDto;
import com.example.demo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto){
        if (requestDto.getTitle() == null || requestDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (requestDto.getContent() == null || requestDto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다");
        }
        if (requestDto.getAuthor() == null || requestDto.getAuthor().isBlank()) {
            throw new IllegalArgumentException("작성자는 필수입니다");
        }

        Post post = Post.create(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor()
        );
        postRepository.save(post);
        return new PostResponseDto(post);
    }
}
