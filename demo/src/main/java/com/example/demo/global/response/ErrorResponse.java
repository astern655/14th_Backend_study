package com.example.demo.global.response;

import com.example.demo.global.exceprion.ErrorCode;

public record ErrorResponse(
    int status,
    String message
)

{

    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());

    }
}
