package com.example.demo.domain.post.dto;

import lombok.Getter;
import com.example.demo.domain.post.entity.Post;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long post_id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponseDto(Post post){
        this.post_id = post.getPostID();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
