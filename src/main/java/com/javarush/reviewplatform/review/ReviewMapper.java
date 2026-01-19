package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends BaseMapper<Review, ReviewTo> {
}
