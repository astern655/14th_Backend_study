package com.example.demo.domain.post.repository;

import com.example.demo.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository <Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}
