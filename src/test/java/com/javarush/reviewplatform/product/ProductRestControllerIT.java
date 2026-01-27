package com.javarush.reviewplatform.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.javarush.reviewplatform.category.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductRestControllerIT extends ContainerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestDataService testDataService;

    @Test
    @WithMockUser
        // Обычный пользователь может просматривать товары
    void should_getAllProducts() throws Exception {
        Category category = testDataService.createCategory();
        testDataService.createProduct(category);

        MvcResult result = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn();

        List<ProductTo> products = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertThat(products).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_createProduct() throws Exception {
        Category category = testDataService.createCategory();

        ProductTo productTo = ProductTo.builder()
                .title("Gaming Mouse")
                .description("High precision sensor")
                .categoryId(category.getId())
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Gaming Mouse"))
                .andExpect(jsonPath("$.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
        // Обычному пользователю запрещено
    void should_forbidCreateProduct_when_User() throws Exception {
        Category category = testDataService.createCategory();
        ProductTo productTo = ProductTo.builder()
                .title("Forbidden")
                .description("High precision sensor")
                .categoryId(category.getId())
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTo)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void should_updateProduct() throws Exception {
        Category category = testDataService.createCategory();
        Product existingProduct = testDataService.createProduct(category);

        ProductTo updateTo = ProductTo.builder()
                .id(existingProduct.getId())
                .title("Updated Name")
                .categoryId(category.getId())
                .build();

        mockMvc.perform(put("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_deleteProduct() throws Exception {
        Category category = testDataService.createCategory();
        Product product = testDataService.createProduct(category);

        mockMvc.perform(delete("/api/v1/products/" + product.getId()))
                .andExpect(status().isNoContent());

        // Проверяем, что товара больше нет
        mockMvc.perform(get("/api/v1/products/" + product.getId()))
                .andExpect(status().isNotFound());
    }
}