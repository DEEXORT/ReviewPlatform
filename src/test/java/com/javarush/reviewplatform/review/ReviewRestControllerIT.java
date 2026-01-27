package com.javarush.reviewplatform.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.javarush.reviewplatform.category.Category;
import com.javarush.reviewplatform.product.Product;
import com.javarush.reviewplatform.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@WithMockUser(roles = {"ADMIN"})
class ReviewRestControllerIT extends ContainerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestDataService testDataService;
    @Autowired
    private ReviewMapper reviewMapper;

    private ReviewTo getReviewTo() {
        User user = testDataService.createUser();
        Category category = testDataService.createCategory();
        Product product = testDataService.createProduct(category);
        return ReviewTo.builder()
                .title("title")
                .content("content")
                .userId(user.getId())
                .productId(product.getId())
                .rating(5)
                .build();
    }

    private ReviewTo createReviewInRepository() {
        User user = testDataService.createUser();
        Category category = testDataService.createCategory();
        Product product = testDataService.createProduct(category);
        Review review = testDataService.createReview(user, product);
        return reviewMapper.mapToDto(review);
    }

    @Test
    void should_getAllReviews() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/reviews"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void should_createReview() throws Exception {
        ReviewTo reviewTo = getReviewTo();

        MvcResult result = mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewTo)))
                .andExpect(status().isCreated())
                .andReturn();
        ReviewTo createdReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewTo.class);

        Assertions.assertEquals(reviewTo.getTitle(), createdReview.getTitle());
        Assertions.assertEquals(reviewTo.getContent(), createdReview.getContent());
        Assertions.assertNotNull(createdReview.getId());
    }

    @Test
    void should_updateReview() throws Exception {
        ReviewTo reviewTo = createReviewInRepository();
        reviewTo.setTitle("updated");


        MvcResult updatedResult = mockMvc.perform(put("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewTo)))
                .andExpect(status().isOk())
                .andReturn();
        ReviewTo updatedReview = objectMapper
                .readValue(updatedResult.getResponse().getContentAsString(), ReviewTo.class);

        Assertions.assertEquals(reviewTo, updatedReview);
    }

    @Test
    void should_getReviewById() throws Exception {
        ReviewTo reviewTo = createReviewInRepository();

        MvcResult getReviewResult = mockMvc.perform(get("/api/v1/reviews/" + reviewTo.getId()))
                .andExpect(status().isOk())
                .andReturn();
        ReviewTo getReviewTo = objectMapper.readValue(getReviewResult.getResponse().getContentAsString(), ReviewTo.class);

        Assertions.assertEquals(reviewTo, getReviewTo);
    }

    @Test
    void should_deleteReviewById() throws Exception {
        ReviewTo reviewTo = createReviewInRepository();

        mockMvc.perform(delete("/api/v1/reviews/" + reviewTo.getId()))
                .andExpect(status().isNoContent());
    }
}