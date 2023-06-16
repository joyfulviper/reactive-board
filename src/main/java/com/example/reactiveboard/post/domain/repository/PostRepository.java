package com.example.reactiveboard.post.domain.repository;

import com.example.reactiveboard.post.domain.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PostRepository extends R2dbcRepository<Post, Long>, CustomPostRepository {
}
