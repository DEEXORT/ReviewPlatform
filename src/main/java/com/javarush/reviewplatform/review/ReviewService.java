package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.AbstractBaseService;
import com.javarush.reviewplatform.product.Product;
import com.javarush.reviewplatform.product.ProductRepository;
import com.javarush.reviewplatform.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService extends AbstractBaseService<Review, ReviewTo, ReviewRepository, ReviewMapper> {
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository repository, ReviewMapper mapper, ProductRepository productRepository) {
        super(repository, mapper);
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public ReviewTo save(ReviewTo reviewTo) {
        ReviewTo saved = super.save(reviewTo);
        Product updatedProduct = updateProduct(saved.getProduct());
        saved.setProduct(updatedProduct);
        return saved;
    }

    private Product updateProduct(Product product) {
        List<Review> reviews = repository.findByProduct(product);
        double avgRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        product.setRating(Math.round(avgRating * 10.0) / 10.0);
        product.setReviewCount(reviews.size());
        return productRepository.save(product);
    }
}
