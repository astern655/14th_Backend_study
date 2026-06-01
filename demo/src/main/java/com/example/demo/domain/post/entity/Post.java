package com.example.demo.domain.post.entity;

import com.example.demo.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postID;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable=false)
    private String content;

    @Column(nullable = false)
    private String author;

    public static Post create(String title, String content, String author){
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

    public void update(String title, String content) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}
