package com.example.reactiveboard.post.application.dto;

import com.example.reactiveboard.post.domain.Post;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class PostRequest {

    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 50, message = "50자 이내의 제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    public Post toEntity(Long authorId) {
        return Post.builder()
                .content(content)
                .title(title)
                .authorId(authorId)
                .build();
    }
}
