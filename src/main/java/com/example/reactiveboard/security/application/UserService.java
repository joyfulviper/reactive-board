package com.example.reactiveboard.security.application;

import com.example.reactiveboard.security.application.dto.AuthRequest;
import com.example.reactiveboard.security.application.dto.UserInfo;
import com.example.reactiveboard.security.application.utils.PBKDF2Encoder;
import com.example.reactiveboard.security.domain.Role;
import com.example.reactiveboard.security.domain.User;
import com.example.reactiveboard.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private Map<String, User> data;

    private final UserRepository userRepository;

    private final PBKDF2Encoder passwordEncoder;

    private final TransactionalOperator operator;

    @PostConstruct
    public void init() {
        data = new HashMap<>();

        //username:passwowrd -> user:user
        data.put("user", new User(1L, "user", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, Role.ROLE_USER));

        //username:passwowrd -> admin:admin
        data.put("admin", new User(2L, "admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, Role.ROLE_ADMIN));
    }

    public Mono<Void> save(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user)
                .as(operator::transactional)
                .then();
    }

    public Mono<UserInfo> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserInfo::from);
    }
}
