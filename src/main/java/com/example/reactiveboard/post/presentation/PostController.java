package com.example.reactiveboard.post.presentation;

import com.example.reactiveboard.post.application.PostService;
import com.example.reactiveboard.post.application.dto.PostRequest;
import com.example.reactiveboard.post.application.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public Mono<ResponseEntity<List<PostResponse>>> findAll(@PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.findAll(pageable).collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> save(@RequestBody Mono<PostRequest> request, ServerWebExchange exchange) {
        exchange.getRequest().getHeaders().getFirst("Authorization");
        return request.flatMap(postService::save)
                .thenReturn(ResponseEntity.created(URI.create("/")).build());
    }
}
