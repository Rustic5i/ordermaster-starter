package dev.ordermaster.starter.config.web.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import dev.ordermaster.starter.config.web.security.authentication.JwtAuthentication;
import dev.ordermaster.starter.config.web.security.managers.CustomAuthenticationManager;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. создайте объект аутентификации, который еще не прошел проверку подлинности
        // 2. делегируйте объект аутентификации менеджеру
        // 3. верните аутентификацию от менеджера
        // 4. если объект аутентифицирован, отправьте запрос следующему фильтру в цепочке

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }

        JwtAuthentication jwtAuthentication = new JwtAuthentication(authHeader, false);

        Authentication authentication = customAuthenticationManager.authenticate(jwtAuthentication);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response); // только тогда, когда аутентификация сработала
        }
    }
}