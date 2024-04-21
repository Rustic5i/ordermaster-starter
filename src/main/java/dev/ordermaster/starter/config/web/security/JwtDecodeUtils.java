package dev.ordermaster.starter.config.web.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class JwtDecodeUtils {

    private static final String REGEX = "\\.";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getPayload(String jwtToken, Class<T> payloadType) throws JsonProcessingException {
        String[] parts = jwtToken.split(REGEX);
        if (parts[1] == null) {
            return null;
        }
        String JwtPayload = parts[1];
        String payloadJson = new String(Base64.getDecoder().decode(JwtPayload));
        return objectMapper.readValue(payloadJson, payloadType);
    }
}