package dev.ordermaster.starter.config.web.security.managers;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final List<AuthenticationProvider> provider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<AuthenticationProvider> authenticationProvider = findAuthenticationProvider(authentication);

        if (authenticationProvider.isPresent()) {
            return authenticationProvider.get().authenticate(authentication);
        }

        throw new ProviderNotFoundException("Провайдер аутентификации не был найден");
    }

    private Optional<AuthenticationProvider> findAuthenticationProvider(Authentication authentication) {
        return Optional.ofNullable(provider)
                .orElse(new ArrayList<>())
                .stream()
                .filter(provider -> provider.supports(authentication.getClass()))
                .findFirst();
    }
}