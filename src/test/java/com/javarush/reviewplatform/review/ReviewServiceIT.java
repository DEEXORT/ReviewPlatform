package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.ContainerIT;
import com.javarush.reviewplatform.TestDataService;
import com.javarush.reviewplatform.category.Category;
import com.javarush.reviewplatform.product.Product;
import com.javarush.reviewplatform.product.ProductMapper;
import com.javarush.reviewplatform.product.ProductService;
import com.javarush.reviewplatform.product.ProductTo;
import com.javarush.reviewplatform.user.User;
import com.javarush.reviewplatform.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class ReviewServiceIT extends ContainerIT {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TestDataService testDataService;

    @Test
    void should_saveReview() {
        User user = testDataService.createUser();
        Category category = testDataService.createCategory();
        Product product = testDataService.createProduct(category);
        ReviewTo reviewTo = ReviewTo.builder()
                .title("Review Title")
                .content("Review Content")
                .productId(product.getId())
                .userId(user.getId())
                .rating(5)
                .build();

        ReviewTo saved = reviewService.save(reviewTo);

        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
    }

    @Test
    void should_saveReviewsAndCalculateAverageRatingCorrectly() {
        User user = testDataService.createUser();
        User user2 = testDataService.createUser();
        Category category = testDataService.createCategory();
        Product product = testDataService.createProduct(category);
        ReviewTo reviewTo = ReviewTo.builder()
                .title("Review Title")
                .content("Review Content")
                .productId(product.getId())
                .userId(user.getId())
                .rating(5)
                .build();
        ReviewTo reviewTo2 = ReviewTo.builder()
                .title("Review Title2")
                .content("Review Content2")
                .productId(product.getId())
                .userId(user.getId())
                .rating(7)
                .build();

        ReviewTo saved = reviewService.save(reviewTo);
        ReviewTo saved2 = reviewService.save(reviewTo2);

        assertNotNull(saved.getId());
        assertNotNull(saved2.getId());
        ProductTo updatedProduct = productService.getById(product.getId());
        assertEquals(6, updatedProduct.getRating());
    }

}