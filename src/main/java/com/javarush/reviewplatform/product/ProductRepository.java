package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.category.Category;
import com.javarush.reviewplatform.common.BaseRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ProductRepository extends BaseRepository<Product> {
    Stream<Product> findByCategoryId(Long categoryId);
}
