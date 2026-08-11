package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.product.model.Product;
import com.javarush.reviewplatform.product.model.ProductTo;
import com.javarush.reviewplatform.product.model.RatingStatistics;
import com.javarush.reviewplatform.product.service.ProductService;
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

    @Mock
    private RatingStatistics ratingStatistics;

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

        // super.save()
        when(mapper.mapToEntity(inputDto))
                .thenReturn(reviewEntity);

        when(repository.save(reviewEntity))
                .thenReturn(reviewEntity);

        when(mapper.mapToDto(reviewEntity))
                .thenReturn(inputDto);

        ProductTo productTo = new ProductTo();
        productTo.setId(productId);

        when(productService.getById(productId))
                .thenReturn(productTo);

        // Результат SQL AVG + COUNT
        when(repository.getRatingStatistics(productId))
                .thenReturn(ratingStatistics);

        when(ratingStatistics.getAvg())
                .thenReturn(6.04);

        when(ratingStatistics.getCount())
                .thenReturn(2L);

        when(productService.save(productTo))
                .thenReturn(productTo);

        // WHEN
        ReviewTo result = reviewService.save(inputDto);

        // THEN
        assertEquals(inputDto, result);

        // ReviewService округляет рейтинг до одного знака
        assertEquals(6.0, productTo.getRating());
        assertEquals(2, productTo.getReviewCount());

        verify(repository).getRatingStatistics(productId);
        verify(productService).save(productTo);
    }

    @Test
    void should_HandleEmptyReviews_WhenCalculatingRating() {
        // GIVEN
        Long productId = 2L;

        ReviewTo inputDto = new ReviewTo();
        inputDto.setProductId(productId);

        Review reviewEntity = new Review();

        when(mapper.mapToEntity(inputDto))
                .thenReturn(reviewEntity);

        when(repository.save(reviewEntity))
                .thenReturn(reviewEntity);

        when(mapper.mapToDto(reviewEntity))
                .thenReturn(inputDto);

        ProductTo productTo = new ProductTo();
        productTo.setId(productId);

        when(productService.getById(productId))
                .thenReturn(productTo);

        when(repository.getRatingStatistics(productId))
                .thenReturn(ratingStatistics);

        when(ratingStatistics.getAvg())
                .thenReturn(0.0);

        when(ratingStatistics.getCount())
                .thenReturn(0L);

        when(productService.save(productTo))
                .thenReturn(productTo);

        // WHEN
        ReviewTo result = reviewService.save(inputDto);

        // THEN
        assertEquals(inputDto, result);
        assertEquals(0.0, productTo.getRating());
        assertEquals(0, productTo.getReviewCount());

        verify(repository).getRatingStatistics(productId);
        verify(productService).save(productTo);
    }
}