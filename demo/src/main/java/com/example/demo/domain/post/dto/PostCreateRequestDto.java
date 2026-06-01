package com.example.demo.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "게시글 등록 요청 DTO")
public record PostCreateRequestDto(
    @Schema(description = "게시글 제목", example = "게시글 제목입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "제목은 필수입니다")
    String title,

    @Schema(description = "게시글 내용", example = "게시글 내용입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "내용은 필수입니다")
    String content,

    @Schema(description = "작성자", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "작성자는 필수입니다")
    String author
) {}
