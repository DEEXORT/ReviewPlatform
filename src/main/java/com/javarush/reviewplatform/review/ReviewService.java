package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.AbstractBaseService;
import com.javarush.reviewplatform.product.ProductService;
import com.javarush.reviewplatform.product.ProductTo;
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
        List<Review> reviews = repository.findByProductId(productId);

        double avgRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        product.setRating(Math.round(avgRating * 10.0) / 10.0);
        product.setReviewCount(reviews.size());

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
