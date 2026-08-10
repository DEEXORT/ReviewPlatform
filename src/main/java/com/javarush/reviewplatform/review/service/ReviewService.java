package com.javarush.reviewplatform.review.service;

import com.javarush.reviewplatform.common.AbstractBaseService;
import com.javarush.reviewplatform.product.model.RatingStatistics;
import com.javarush.reviewplatform.product.service.ProductService;
import com.javarush.reviewplatform.product.model.ProductTo;
import com.javarush.reviewplatform.review.mapper.ReviewMapper;
import com.javarush.reviewplatform.review.model.Review;
import com.javarush.reviewplatform.review.model.ReviewTo;
import com.javarush.reviewplatform.review.model.ReviewViewTo;
import com.javarush.reviewplatform.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService extends AbstractBaseService<Review, ReviewTo, ReviewRepository, ReviewMapper> {
    private final ProductService productService;

    public ReviewService(ReviewRepository repository, ReviewMapper mapper, ProductService productService) {
        super(repository, mapper);
        this.productService = productService;
    }

    @Transactional
    @Override
    public ReviewTo save(ReviewTo reviewTo) {
        ReviewTo saved = super.save(reviewTo);
        ProductTo updatedProduct = updateProduct(saved.getProductId());
        return saved;
    }

    private ProductTo updateProduct(Long productId) {
        ProductTo product = productService.getById(productId);

        RatingStatistics stats = repository.getRatingStatistics(productId);
        product.setRating(Math.round(stats.getAvg() * 10.0) / 10.0);
        product.setReviewCount(Math.toIntExact(stats.getCount()));

        return productService.save(product);
    }

    public List<ReviewViewTo> getReviewViewsByUsername(String username) {
        List<Review> reviews = repository.findByUserUsername(username);
        return reviews.stream().map(mapper::mapToViewDto).toList();
    }

    public List<ReviewViewTo> getAllReviewViews() {
        return repository.findAll().stream().map(mapper::mapToViewDto).toList();
    }
}
