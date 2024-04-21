package dev.ordermaster.starter.config.web.security.providers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ordermaster.starter.config.web.security.JwtDecodeUtils;
import dev.ordermaster.starter.config.web.security.JwtInfoPayload;
import dev.ordermaster.starter.config.web.security.authentication.JwtAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        String jwt = jwtAuthentication.getJwt();
        JwtInfoPayload jwtInfoPayload = null;

        try {
            jwtInfoPayload = JwtDecodeUtils.getPayload(jwt, JwtInfoPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Не валидный токен", e);
            throw new RuntimeException("Не валидный токен");
        }

        checkPayload(jwtInfoPayload);

        return createClientAuthenticationToken(jwtInfoPayload, jwt);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }

    private JwtAuthentication createClientAuthenticationToken(JwtInfoPayload jwtInfoPayload, String jwt) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwt, true);
        jwtAuthentication.setClientId(jwtInfoPayload.getClientId());
        jwtAuthentication.setPhoneNumber(jwtInfoPayload.getPhoneNumber());
        jwtAuthentication.setRoles(jwtInfoPayload.getRoles());
        return jwtAuthentication;
    }

    private void checkPayload(JwtInfoPayload jwtInfoPayload) {
        if (Objects.isNull(jwtInfoPayload)
                // todo под вопросом.
                || LocalDateTime.ofInstant(Instant.ofEpochSecond(jwtInfoPayload.getExp()), ZoneId.of("UTC")).isBefore(LocalDateTime.now())
                || StringUtils.isBlank(jwtInfoPayload.getClientId())
                || StringUtils.isBlank(jwtInfoPayload.getPhoneNumber())) {
            throw new RuntimeException("Не валидный токен");
        }
    }
}