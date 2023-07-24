package com.example.reactiveboard.post.application;

import com.example.reactiveboard.post.application.dto.PostRequest;
import com.example.reactiveboard.post.application.dto.PostResponse;
import com.example.reactiveboard.post.domain.repository.PostRepository;
import com.example.reactiveboard.security.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final TransactionalOperator operator;

    public Flux<PostResponse> findAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponse::from);
    }

    public Mono<Void> save(PostRequest request, String username) {
        return userService.findByUsername(username)
                .flatMap(userInfo -> postRepository.save(request.toEntity(userInfo.getId()))
                        .as(operator::transactional))
                .then();
    }

    public Mono<PostResponse> update(Long id, PostRequest request, String username) {
        return isPostAuthor(username, id)
                .flatMap(isAuthor -> {
                    if (isAuthor) {
                        return postRepository.findById(id)
                                .flatMap(post -> {
                                    post.update(request.getTitle(), request.getContent());
                                    return postRepository.save(post);
                                })
                                .as(operator::transactional)
                                .map(PostResponse::from);
                    } else {
                        return Mono.error(new AccessDeniedException("You are not the author of this post"));
                    }
                });
    }

    public Mono<Void> deleteById(Long id, String username) {
        return isPostAuthor(username, id)
                .flatMap(isAuthor -> {
                    if (isAuthor) {
                        return postRepository.deleteById(id)
                                .then(Mono.empty());
                    } else {
                        return Mono.error(new AccessDeniedException("You are not the author of this post"));
                    }
                });
    }

    public Mono<PostResponse> findById(Long id) {
        return postRepository.findById(id)
                .map(PostResponse::from);
    }

    private Mono<Boolean> isPostAuthor(String username, Long id) {
        return userService.findByUsername(username)
                .flatMap(user -> findById(id)
                        .map(post -> user.getId().equals(post.getAuthorId())));
    }
}
