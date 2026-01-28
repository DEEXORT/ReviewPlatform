package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryTo> {
}
