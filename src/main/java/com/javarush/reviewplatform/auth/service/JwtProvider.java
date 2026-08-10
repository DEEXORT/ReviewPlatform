package com.javarush.reviewplatform.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private final String secret;

    @Getter
    private final long expiration;

    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long exp) {
        this.secret = secret;
        this.expiration = exp;
    }

    public String generateToken(CustomUserDetails userDetails, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", roles)
                .withClaim("userId", userDetails.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }
}
