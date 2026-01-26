package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends BaseMapper<Review, ReviewTo> {
    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product.id", source = "productId")
    Review mapToEntity(ReviewTo to);

    @Override
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    ReviewTo mapToDto(Review entity);
}
