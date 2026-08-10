package com.javarush.reviewplatform.category.mapper;

import com.javarush.reviewplatform.category.model.Category;
import com.javarush.reviewplatform.category.model.CategoryTo;
import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryTo> {
}
