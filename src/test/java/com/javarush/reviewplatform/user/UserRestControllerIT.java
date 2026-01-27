package com.javarush.reviewplatform.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Slf4j
@WithMockUser(roles = {"ADMIN"})
class UserRestControllerIT extends ContainerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataService testDataService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void should_getUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void should_getUserById() throws Exception {
        User user = testDataService.createUser();

        MvcResult result = mockMvc.perform(get("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andReturn();
    }

    @Test
    void should_createUser() throws Exception {
        UserTo userTo = getUserTo();
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTo))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void should_updateUser() throws Exception {
        User user = testDataService.createUser();

        UserTo userTo = userMapper.mapToDto(user);
        userTo.setPassword("newPassword");
        userTo.setConfirmPassword("newPassword");

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTo)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        User user = testDataService.createUser();

        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }

    private static UserTo getUserTo() {
        return UserTo.builder()
                .username("testUsername")
                .password("testPassword")
                .confirmPassword("testPassword")
                .email("testEmail@test.ru")
                .firstName("testFirstName")
                .lastName("testLastName")
                .build();
    }
}