package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 DTO")
public record LoginResponseDto(
    @Schema(description = "사용자 ID", example = "1")
    Long id,

    @Schema(description = "사용자 이메일", example = "test@example.com")
    String email
) {
    public LoginResponseDto(UserEntity user) {
        this(user.getId(), user.getEmail());
    }
}
