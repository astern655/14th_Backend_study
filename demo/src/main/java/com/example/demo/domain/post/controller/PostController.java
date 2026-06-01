package com.example.demo.domain.post.controller;

import com.example.demo.domain.post.dto.PostCreateRequestDto;
import com.example.demo.domain.post.dto.PostCreateResponseDto;
import com.example.demo.domain.post.dto.PostDeleteResponseDto;
import com.example.demo.domain.post.dto.PostGetResponseDto;
import com.example.demo.domain.post.dto.PostListResponseDto;
import com.example.demo.domain.post.dto.PostUpdateRequestDto;
import com.example.demo.domain.post.dto.PostUpdateResponseDto;
import com.example.demo.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 API", description = "게시글 등록, 조회, 수정, 삭제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 등록", description = "새로운 게시글을 작성하고 등록합니다.")
    @PostMapping
    public ResponseEntity<PostCreateResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto requestDto){
        PostCreateResponseDto responseDto = postService.createPost(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 생성일(최신순) 기준으로 정렬하여 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {
        List<PostListResponseDto> responseDtoList = postService.getAllPosts();
        return ResponseEntity.ok(responseDtoList);
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 ID의 게시글 상세 내용을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponseDto> getPost(@PathVariable("postId") Long postId) {
        PostGetResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "게시글 수정", description = "특정 ID의 게시글 제목 및 내용을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost(@PathVariable("postId") Long postId,
                                                            @RequestBody PostUpdateRequestDto requestDto) {
        PostUpdateResponseDto responseDto = postService.updatePost(postId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "게시글 삭제", description = "특정 ID의 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostDeleteResponseDto> deletePost(@PathVariable("postId") Long postId) {
        PostDeleteResponseDto responseDto = postService.deletePost(postId);
        return ResponseEntity.ok(responseDto);
    }
}

