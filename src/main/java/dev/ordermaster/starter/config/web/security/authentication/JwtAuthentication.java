package dev.ordermaster.starter.config.web.security.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class JwtAuthentication implements Authentication {

    private String jwt;

    private boolean isAuthentication;

    private String clientId;

    private String phoneNumber;

    private List<String> roles = new ArrayList<>();

    public JwtAuthentication(String jwt, boolean isAuthentication) {
        this.jwt = jwt;
        this.isAuthentication = isAuthentication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthentication;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthentication = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }
}
