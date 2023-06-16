package com.example.reactiveboard.post.domain.repository;

import com.example.reactiveboard.post.domain.Post;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface CustomPostRepository {
    Flux<Post> findAll(Pageable pageable);
}
