package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends BaseMapper<Review, ReviewTo> {
    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product.id", source = "productId")
    Review mapToEntity(ReviewTo to);

    @Override
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    @Named(value = "EntityToDto")
    ReviewTo mapToDto(Review entity);

    @Override
    @IterableMapping(qualifiedByName = "EntityToDto")
    List<ReviewTo> mapToDtoList(List<Review> entities);

    @Mapping(target = "categoryName", source = "product.category.name")
    @Mapping(target = "productName", source = "product.title")
    @Mapping(target = "username", source = "user.username")
    ReviewViewTo mapToViewDto(Review entity);
}
