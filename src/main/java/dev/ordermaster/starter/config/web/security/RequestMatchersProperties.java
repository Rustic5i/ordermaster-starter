package dev.ordermaster.starter.config.web.security;

import lombok.Data;

import java.util.List;

@Data
public class RequestMatchersProperties {
    private List<String> roles;
    private List<String> urlPatterns;
}
