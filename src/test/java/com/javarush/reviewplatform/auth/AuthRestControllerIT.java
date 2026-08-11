package com.javarush.reviewplatform.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.javarush.reviewplatform.auth.model.AuthRequest;
import com.javarush.reviewplatform.auth.model.AuthResponse;
import com.javarush.reviewplatform.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthRestControllerIT extends ContainerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataService testDataService;

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        // GIVEN: Создаем реального пользователя в тестовой БД Postgres
        // Предполагаем, что TestDataService сохраняет пароль в зашифрованном виде "password"
        User user = testDataService.createUser("auth_user", "password");

        AuthRequest loginRequest = new AuthRequest(user.getUsername(), "password");

        // WHEN
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // THEN
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, org.hamcrest.Matchers.startsWith("Bearer ")))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenPasswordIsWrong() throws Exception {
        // GIVEN
        User user = testDataService.createUser("wrong_pass_user", "wrong_pass_password");
        AuthRequest loginRequest = new AuthRequest(user.getUsername(), "wrong_password");

        // WHEN
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_ShouldReturnUnauthorized_WhenTokenIsMissing()
            throws Exception {

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_ShouldReturnOk_WhenTokenIsValid() throws Exception {
        User user = testDataService.createUser("auth_user", "password");
        AuthRequest loginRequest = new AuthRequest(user.getUsername(), "password");
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);

        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_ShouldReturnUnauthorized_WhenJwtIsTampered() throws Exception {
        User user = testDataService.createUser("auth_user", "password");
        AuthRequest loginRequest = new AuthRequest(user.getUsername(), "password");
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);

        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getToken() + "tampered"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_ShouldReturnUnauthorized_WhenTokenIsExpired()
            throws Exception {

        Algorithm algorithm = Algorithm.HMAC256("test-jwt-secret-for-integration-tests");

        String expiredToken = JWT.create()
                .withSubject("expired_user")
                .withClaim("roles", List.of("USER"))
                .withIssuedAt(new Date(System.currentTimeMillis() - 60_000))
                .withExpiresAt(new Date(System.currentTimeMillis() - 1_000))
                .sign(algorithm);

        mockMvc.perform(get("/api/v1/products")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + expiredToken
                        ))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpoint_ShouldReturnForbidden_WhenUserHasNoAdminRole()
            throws Exception {

        User user = testDataService.createUser("regular_user", "password");

        AuthRequest loginRequest =
                new AuthRequest(user.getUsername(), "password");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(response)
                .get("token")
                .asText();

        mockMvc.perform(delete("/api/v1/users/" + user.getId())
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isForbidden());
    }
}