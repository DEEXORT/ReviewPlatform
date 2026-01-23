package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.BaseRepository;
import com.javarush.reviewplatform.product.Product;

import java.util.List;

public interface ReviewRepository extends BaseRepository<Review> {
    List<Review> findByProduct(Product product);
}
