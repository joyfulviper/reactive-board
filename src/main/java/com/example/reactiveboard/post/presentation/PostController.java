package com.example.reactiveboard.post.presentation;

import com.example.reactiveboard.post.application.PostService;
import com.example.reactiveboard.post.application.dto.PostRequest;
import com.example.reactiveboard.post.application.dto.PostResponse;
import com.example.reactiveboard.security.application.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public Mono<ResponseEntity<List<PostResponse>>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return postService.findAll(pageable).collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> save(@Valid @RequestBody Mono<PostRequest> request, Principal principal) {
        return request.flatMap(req -> postService.save(req, principal.getName()))
                .thenReturn(ResponseEntity.created(URI.create("/")).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> update(@PathVariable("id") Long id, @Valid @RequestBody Mono<PostRequest> request, Principal principal) {
        return request.flatMap(req -> postService.update(id, req, principal.getName()))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") Long id, Principal principal) {
        return postService.deleteById(id, principal.getName())
                .thenReturn(ResponseEntity.noContent().build());
    }
}
