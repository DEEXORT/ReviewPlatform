package com.javarush.reviewplatform.review.repository;

import com.javarush.reviewplatform.common.BaseRepository;
import com.javarush.reviewplatform.product.model.Product;
import com.javarush.reviewplatform.product.model.RatingStatistics;
import com.javarush.reviewplatform.review.model.Review;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends BaseRepository<Review> {
    List<Review> findByProduct(Product product);

    List<Review> findByProductId(Long productId);

    List<Review> findByUserUsername(String username);

    @Query("""
                select count(r) as count, avg(r.rating) as avg
                from Review r
                where r.product.id = :productId
            """)
    RatingStatistics getRatingStatistics(Long productId);
}
