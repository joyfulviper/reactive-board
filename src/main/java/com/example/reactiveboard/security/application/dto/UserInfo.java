package com.example.reactiveboard.security.application.dto;

import com.example.reactiveboard.security.domain.Role;
import com.example.reactiveboard.security.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserInfo {
    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private Role role;

    public static UserInfo from(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.getEnabled())
                .role(user.getRole())
                .build();
    }
}
