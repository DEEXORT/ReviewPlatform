package com.javarush.reviewplatform.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
    private final String username;
    private final List<String> roles;
    @JsonProperty("token_type")
    private final String tokenType;
    private final String token;
    @JsonProperty("expires_in")
    private final long expiresIn;
}
