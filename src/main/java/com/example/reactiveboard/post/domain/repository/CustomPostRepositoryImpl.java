package com.example.reactiveboard.post.domain.repository;

import com.example.reactiveboard.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Post> findAll(Pageable pageable) {
        return databaseClient.sql("SELECT * FROM post LIMIT :limit OFFSET :offset")
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset())
                .map((row, metadata) -> Post.builder()
                        .id(row.get("id", Long.class))
                        .title(row.get("title", String.class))
                        .content(row.get("content", String.class))
                        .authorId(row.get("authorId", Long.class))
                        .build())
                .all();
    }
}
