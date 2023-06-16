package com.example.reactiveboard.post.application.dto;

import com.example.reactiveboard.post.domain.Post;
import lombok.Getter;

@Getter
public class PostRequest {
    private String title;
    private String content;
    private Long authId;

    public Post toEntity() {
        return Post.builder()
                .content(content)
                .title(title)
                .authorId(authId)
                .build();
    }
}
