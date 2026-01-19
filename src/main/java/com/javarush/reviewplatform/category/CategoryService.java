package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.common.AbstractBaseService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbstractBaseService<Category, CategoryTo , CategoryRepository, CategoryMapper> {

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        super(repository, mapper);
    }
}
