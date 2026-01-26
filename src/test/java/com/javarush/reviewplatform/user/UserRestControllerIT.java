package com.javarush.reviewplatform.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@Transactional
@Slf4j
@RequiredArgsConstructor
class UserRestControllerIT extends ContainerIT {
    private final MockMvc mockMvc;
    private final TestDataService testDataService;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andReturn();
        this.token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    @Test
    void should_getUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void should_getUserById() throws Exception {
        User user = testDataService.createUser();

        MvcResult result = mockMvc.perform(get("/api/v1/users/" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        UserTo userTo = objectMapper.readValue(result.getResponse().getContentAsString(), UserTo.class);
        Assertions.assertEquals(userMapper.mapToDto(user), userTo);
    }

    @Test
    void should_createUser() throws Exception {
        UserTo userTo = getUserTo();
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTo))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserTo createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserTo.class);
        Assertions.assertEquals(userTo.getUsername(), createdUser.getUsername());
        Assertions.assertNotNull(createdUser.getId());
    }

    @Test
    void updateUser() throws Exception {
        UserTo userTo = getUserTo();
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTo))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserTo createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserTo.class);
        createdUser.setPassword("newPassword");
        createdUser.setConfirmPassword("newPassword");

        MvcResult updatedUserResult = mockMvc.perform(put("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser)))
                .andExpect(status().isOk())
                .andReturn();
        UserTo updatedUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserTo.class);
        Assertions.assertEquals(createdUser, updatedUser);
    }

    @Test
    void deleteUser() throws Exception {
        UserTo userTo = getUserTo();
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTo))
                )
                .andExpect(status().isOk())
                .andReturn();
        UserTo createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserTo.class);

        mockMvc.perform(delete("/api/v1/users/" + createdUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private static UserTo getUserTo() {
        UserTo userTo = UserTo.builder()
                .username("testUsername")
                .password("testPassword")
                .confirmPassword("testPassword")
                .email("testEmail@test.ru")
                .firstName("testFirstName")
                .lastName("testLastName")
                .build();
        return userTo;
    }
}