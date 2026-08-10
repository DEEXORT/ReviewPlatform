package com.javarush.reviewplatform.review.repository;

import com.javarush.reviewplatform.common.BaseRepository;
import com.javarush.reviewplatform.product.model.Product;
import com.javarush.reviewplatform.review.model.Review;

import java.util.List;

public interface ReviewRepository extends BaseRepository<Review> {
    List<Review> findByProduct(Product product);

    List<Review> findByProductId(Long productId);

    List<Review> findByUserUsername(String username);
}
