package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.product.model.Product;
import com.javarush.reviewplatform.product.service.ProductService;
import com.javarush.reviewplatform.product.model.ProductTo;
import com.javarush.reviewplatform.review.mapper.ReviewMapper;
import com.javarush.reviewplatform.review.model.Review;
import com.javarush.reviewplatform.review.model.ReviewTo;
import com.javarush.reviewplatform.review.repository.ReviewRepository;
import com.javarush.reviewplatform.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private ReviewMapper mapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void should_CalculateAverageRatingAndCount_WhenSavingReview() {
        // GIVEN
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .build();
        ReviewTo inputDto = new ReviewTo();
        inputDto.setProductId(productId);
        inputDto.setRating(5);

        Review reviewEntity = new Review();
        reviewEntity.setProduct(product);
        reviewEntity.setRating(5);

        // Настраиваем поведение базового сохранения (super.save)
        when(mapper.mapToEntity(inputDto)).thenReturn(reviewEntity);
        when(repository.save(reviewEntity)).thenReturn(reviewEntity);
        when(mapper.mapToDto(reviewEntity)).thenReturn(inputDto);

        // Данные для пересчета рейтинга
        ProductTo productTo = new ProductTo();
        productTo.setId(productId);

        // Имитируем, что в базе уже есть два отзыва с рейтингами 4 и 8
        Review r1 = new Review(); r1.setRating(4);
        Review r2 = new Review(); r2.setRating(8);
        List<Review> existingReviews = List.of(r1, r2);

        when(productService.getById(productId)).thenReturn(productTo);
        when(repository.findByProductId(productId)).thenReturn(existingReviews);
        when(productService.save(any(ProductTo.class))).thenReturn(productTo);

        // WHEN
        ReviewTo result = reviewService.save(inputDto);

        // THEN
        // Среднее арифметическое (4 + 8) / 2 = 6.0
        assertEquals(6.0, productTo.getRating());
        assertEquals(2, productTo.getReviewCount());

        // Проверяем, что продукт был сохранен с обновленными данными
        verify(productService).save(productTo);
        verify(repository).findByProductId(productId);
    }

    @Test
    void should_HandleEmptyReviews_WhenCalculatingRating() {
        // GIVEN
        Long productId = 2L;
        ReviewTo reviewTo = new ReviewTo();
        reviewTo.setProductId(productId);

        Review reviewEntity = new Review();
        when(mapper.mapToEntity(any())).thenReturn(reviewEntity);
        when(repository.save(any())).thenReturn(reviewEntity);
        when(mapper.mapToDto(any())).thenReturn(reviewTo);

        ProductTo productTo = new ProductTo();
        when(productService.getById(productId)).thenReturn(productTo);
        when(repository.findByProductId(productId)).thenReturn(List.of()); // Нет отзывов
        when(productService.save(any())).thenReturn(productTo);

        // WHEN
        reviewService.save(reviewTo);

        // THEN
        assertEquals(0.0, productTo.getRating());
        assertEquals(0, productTo.getReviewCount());
    }
}