package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답 DTO")
public record SignUpResponseDto(
    @Schema(description = "사용자 ID", example = "1")
    Long id,

    @Schema(description = "사용자 이메일", example = "test@example.com")
    String email
) {
    public SignUpResponseDto(UserEntity user) {
        this(user.getId(), user.getEmail());
    }
}
