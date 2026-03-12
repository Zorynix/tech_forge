package com.techmarket.authservice.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

public class UserPrincipal extends AbstractAuthenticationToken {

    private final UUID userId;
    private final String phone;

    public UserPrincipal(UUID userId, String phone) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = userId;
        this.phone = phone;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public String getName() {
        return phone;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }
}
