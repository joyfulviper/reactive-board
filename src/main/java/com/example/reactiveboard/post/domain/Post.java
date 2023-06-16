package com.example.reactiveboard.post.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("post")
@Getter
@Builder
public class Post {

    @Id
    private Long id;

    private String title;

    private String content;

    private Long authorId;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
