package dev.ordermaster.starter.config.web.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "ordermaster-starter.web.security")
public class SecurityProperties {

    private List<RequestMatchersProperties> requestMatchers;
}
