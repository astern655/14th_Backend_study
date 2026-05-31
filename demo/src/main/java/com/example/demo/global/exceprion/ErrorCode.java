package com.example.demo.global.exceprion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404," 존재하지 않는 이메일입니다"),
    INVALID_PASSWORD(401,"이메일 또는 비밀번호가 틀렸습니다"),
    DUPLICATE_EMAIL(409, "이미 사용중인 이메일입니다"),
    INVALID_INPUT(400,"입력값이 올바르지 않습니다"),
    POST_NOT_FOUND(404, "존재하지 않는 게시글입니다"),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다");


    private final int status;
    private final String message;
}
