package com.example.reactiveboard.post.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("post")
@Getter
@Builder
public class Post {

    @Version
    private Long version;

    @Id
    private Long id;

    private String title;

    private String content;

    @Column("author_id")
    private Long authorId;

    @Column("created_at")
    private LocalDateTime createdAt;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
