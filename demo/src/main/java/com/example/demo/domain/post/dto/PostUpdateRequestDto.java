package com.example.demo.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 수정 요청 DTO")
public record PostUpdateRequestDto(
    @Schema(description = "수정할 게시글 제목", example = "수정된 제목입니다.")
    String title,

    @Schema(description = "수정할 게시글 내용", example = "수정된 내용입니다.")
    String content
) {}
