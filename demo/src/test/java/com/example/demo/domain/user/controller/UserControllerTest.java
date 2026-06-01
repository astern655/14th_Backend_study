package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignUpRequestDto;
import com.example.demo.domain.user.dto.SignUpResponseDto;
import com.example.demo.domain.user.entity.UserEntity;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.config.JwtTokenProvider;
import com.example.demo.global.config.SecurityConfig;
import com.example.demo.global.exceprion.CustomException;
import com.example.demo.global.exceprion.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_Success() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@example.com", "1234");
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        SignUpResponseDto responseDto = new SignUpResponseDto(user);

        when(userService.signUp(any(SignUpRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 누락")
    void signUp_Fail_EmailBlank() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("", "1234");

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다"));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 누락")
    void signUp_Fail_PasswordBlank() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@example.com", "  ");

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 가입된 이메일")
    void signUp_Fail_DuplicateEmail() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@example.com", "1234");
        when(userService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(new CustomException(ErrorCode.DUPLICATE_EMAIL));

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 사용중인 이메일입니다"));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_Success() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "1234");
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        LoginResponseDto responseDto = new LoginResponseDto(user);

        when(userService.login(any(LoginRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 누락")
    void login_Fail_EmailBlank() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("", "1234");

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다"));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 누락")
    void login_Fail_PasswordBlank() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com", " ");

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다"));
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_Fail_UserNotFound() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("nonexistent@example.com", "1234");
        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(" 존재하지 않는 이메일입니다"));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_InvalidPassword() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "wrongpassword");
        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new CustomException(ErrorCode.INVALID_PASSWORD));

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 틀렸습니다"));
    }
}
