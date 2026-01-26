package com.javarush.reviewplatform.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();

        String token;
        token = getJwtTokenFromCookies(request);

        if (token != null) {
            try {
                DecodedJWT verify = verifier.verify(token);

                List<SimpleGrantedAuthority> roles = verify
                        .getClaim("roles")
                        .asList(String.class)
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                        .toList();

                CustomUserDetails userDetails = new CustomUserDetails(
                        verify.getSubject(),
                        "",
                        roles,
                        verify.getClaim("userId").asLong());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JWTVerificationException e) {
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromCookies(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
