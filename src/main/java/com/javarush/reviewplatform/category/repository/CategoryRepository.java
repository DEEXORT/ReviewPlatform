package com.javarush.reviewplatform.category.repository;

import com.javarush.reviewplatform.category.model.Category;
import com.javarush.reviewplatform.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {

    Category getById(Long id);
}
