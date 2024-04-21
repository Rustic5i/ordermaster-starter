package dev.ordermaster.starter.config.web.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class JwtInfoPayload {

    @JsonAlias("client_id")
    private String clientId;

    @JsonAlias("phone_number")
    private String phoneNumber;

    private List<String> roles;

    private String jti;
    private Long exp;
    private Long iat;
}
