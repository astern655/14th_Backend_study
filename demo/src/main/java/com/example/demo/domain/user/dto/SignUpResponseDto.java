package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답 DTO")
public record SignUpResponseDto(
    @Schema(description = "사용자 ID", example = "1")
    Long id,

    @Schema(description = "사용자 이메일", example = "test@example.com")
    String email,

    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token
) {
    public SignUpResponseDto(UserEntity user) {
        this(user.getId(), user.getEmail(), null);
    }

    public SignUpResponseDto(UserEntity user, String token) {
        this(user.getId(), user.getEmail(), token);
    }
}
