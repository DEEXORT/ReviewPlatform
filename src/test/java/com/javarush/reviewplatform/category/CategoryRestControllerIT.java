package com.javarush.reviewplatform.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryRestControllerIT extends ContainerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataService testDataService;

    @Test
    @WithMockUser // Обычный пользователь
    void should_getAllCategories() throws Exception {
        testDataService.createCategory(); // Создаем тестовые данные в БД

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void should_createCategory_whenUserIsManager() throws Exception {
        CategoryTo categoryTo = new CategoryTo(null, "Electronics", "Gadgets and devices");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_returnForbidden_whenUserIsNormalUser_AndTriesToCreate() throws Exception {
        CategoryTo categoryTo = new CategoryTo(null, "Illegal", "Should not work");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryTo)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_updateCategory() throws Exception {
        Category existing = testDataService.createCategory();
        CategoryTo updateTo = new CategoryTo(existing.getId(), "Updated Name", "Updated Desc");

        mockMvc.perform(put("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_returnBadRequest_whenUpdateWithoutId() throws Exception {
        CategoryTo updateTo = new CategoryTo(null, "No ID", "Bad request");

        mockMvc.perform(put("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_deleteCategory() throws Exception {
        Category existing = testDataService.createCategory();

        mockMvc.perform(delete("/api/v1/categories/" + existing.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_returnNotFound_whenDeleteNonExistentCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/99999"))
                .andExpect(status().isNotFound());
    }
}