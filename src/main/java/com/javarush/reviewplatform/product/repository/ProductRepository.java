package com.javarush.reviewplatform.product.repository;

import com.javarush.reviewplatform.common.BaseRepository;
import com.javarush.reviewplatform.product.model.Product;

import java.util.stream.Stream;

public interface ProductRepository extends BaseRepository<Product> {
    Stream<Product> findByCategoryId(Long categoryId);
}
