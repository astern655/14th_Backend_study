package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignUpRequestDto;
import com.example.demo.domain.user.dto.SignUpResponseDto;
import com.example.demo.domain.user.entity.UserEntity;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exceprion.CustomException;
import com.example.demo.global.exceprion.ErrorCode;
import com.example.demo.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.password());

        UserEntity user = UserEntity.builder()
                .email(requestDto.email())
                .password(encodedPassword)
                .build();

        UserEntity savedUser = userRepository.save(user);
        String token = jwtTokenProvider.createToken(savedUser.getId(), savedUser.getEmail());
        return new SignUpResponseDto(savedUser, token);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto requestDto) {
        UserEntity user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getId(), user.getEmail());
        return new LoginResponseDto(user, token);
    }
}
