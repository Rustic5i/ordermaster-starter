package dev.ordermaster.starter.config.web.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import dev.ordermaster.starter.config.web.security.filters.CustomAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@ComponentScan("dev.ordermaster.starter.config.web")
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.anyRequest().authenticated();
                })
                .build();
    }

    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry getRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        for (RequestMatchersProperties requestMatcher : securityProperties.getRequestMatchers()) {
            authorizeRequests
                    .requestMatchers(requestMatcher.getUrlPatterns().toArray(String[]::new)).hasAnyRole(requestMatcher.getRoles().toArray(String[]::new));
        }
        return authorizeRequests;
    }
}