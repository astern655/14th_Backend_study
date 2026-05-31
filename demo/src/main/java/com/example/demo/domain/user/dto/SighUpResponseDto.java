package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class SighUpResponseDto {
    private Long user_id;
    private String adress;
    private String tk;

    public SighUpResponseDto(UserEntity user , String token) {
        this.user_id = user.getUserId();
        this.adress = user.getAdress();
        this.tk = token;
    }
}
