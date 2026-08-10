package com.javarush.reviewplatform.category.service;

import com.javarush.reviewplatform.category.mapper.CategoryMapper;
import com.javarush.reviewplatform.category.repository.CategoryRepository;
import com.javarush.reviewplatform.category.exception.CategoryNotFoundException;
import com.javarush.reviewplatform.category.model.Category;
import com.javarush.reviewplatform.category.model.CategoryTo;
import com.javarush.reviewplatform.common.AbstractBaseService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbstractBaseService<Category, CategoryTo, CategoryRepository, CategoryMapper> {

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        super(repository, mapper);
    }

    public String getCategoryNameById(Long id) {
        Category category = repository.getById(id);
        if (category == null) throw new CategoryNotFoundException("Category not found");
        return category.getName();
    }
}
