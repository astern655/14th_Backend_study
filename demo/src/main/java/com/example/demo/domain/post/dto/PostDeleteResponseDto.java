package com.example.demo.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 삭제 응답 DTO")
public record PostDeleteResponseDto(
    @Schema(description = "삭제 결과 메시지", example = "게시글이 삭제되었습니다.")
    String message
) {}
