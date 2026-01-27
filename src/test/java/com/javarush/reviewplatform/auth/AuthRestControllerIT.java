package com.javarush.reviewplatform.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.javarush.reviewplatform.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}