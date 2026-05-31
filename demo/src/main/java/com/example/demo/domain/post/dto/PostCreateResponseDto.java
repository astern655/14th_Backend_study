package com.example.demo.domain.post.dto;

import com.example.demo.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 등록 응답 DTO")
public record PostCreateResponseDto(
    @Schema(description = "게시글 ID", example = "1")
    Long id,

    @Schema(description = "게시글 제목", example = "게시글 제목입니다.")
    String title,

    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    String content,

    @Schema(description = "작성자", example = "홍길동")
    String author,

    @JsonProperty("created_at")
    @Schema(description = "작성일", example = "2026-05-31T22:23:50")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    @Schema(description = "수정일", example = "2026-05-31T22:23:50")
    LocalDateTime updatedAt
) {
    public PostCreateResponseDto(Post post) {
        this(
            post.getPostID(),
            post.getTitle(),
            post.getContent(),
            post.getAuthor(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
