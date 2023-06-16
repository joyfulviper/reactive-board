package com.example.reactiveboard.security.presentaition;

import com.example.reactiveboard.security.application.dto.AuthRequest;
import com.example.reactiveboard.security.application.dto.AuthResponse;
import com.example.reactiveboard.security.application.UserService;
import com.example.reactiveboard.security.application.dto.Message;
import com.example.reactiveboard.security.application.utils.JWTUtil;
import com.example.reactiveboard.security.application.utils.PBKDF2Encoder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthenticationREST {

    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
            .filter(userInfo -> passwordEncoder.encode(ar.getPassword()).equals(userInfo.getPassword()))
            .map(userInfo -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userInfo))))
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PostMapping("/sign-up")
    public Mono<ResponseEntity<Void>> signup(@RequestBody Mono<AuthRequest> ar) {
        return ar.flatMap(userService::save)
                .thenReturn(ResponseEntity.created(URI.create("/")).build());
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> getUserInfo(ServerWebExchange exchange) {
        return Mono.justOrEmpty(Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .map(authHeader -> authHeader.replace("Bearer ", ""))
                .map(jwtUtil::getUsernameFromToken)
                .map(username -> ResponseEntity.ok(new Message("User info: " + username)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
