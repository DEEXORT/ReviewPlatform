package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductTo> {
}
