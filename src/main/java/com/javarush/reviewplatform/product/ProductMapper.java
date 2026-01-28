package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductTo> {
    @Override
    @Mapping(target = "category.id", source = "categoryId")
    Product mapToEntity(ProductTo to);

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    @Named(value = "EntityToDto")
    ProductTo mapToDto(Product entity);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductViewTo mapToViewDto(Product entity);

    @Override
    @IterableMapping(qualifiedByName = "EntityToDto")
    List<ProductTo> mapToDtoList(List<Product> entities);
}
