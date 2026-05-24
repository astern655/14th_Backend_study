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
        Post post = Post.create(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor()
        );
        postRepository.save(post);
        return new PostResponseDto(post);
    }
}
