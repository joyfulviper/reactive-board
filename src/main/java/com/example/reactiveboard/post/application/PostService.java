package com.example.reactiveboard.post.application;

import com.example.reactiveboard.post.application.dto.PostRequest;
import com.example.reactiveboard.post.application.dto.PostResponse;
import com.example.reactiveboard.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final TransactionalOperator operator;
    public Flux<PostResponse> findAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponse::from);
    }

    public Mono<Void> save(PostRequest request) {
        return postRepository.save(request.toEntity())
                .as(operator::transactional)
                .then();
    }
}
