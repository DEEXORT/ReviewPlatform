package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserTo> {
    @Override
    User mapToEntity(UserTo to);

    @Override
    @Mapping(target = "confirmPassword", ignore = true)
    UserTo mapToDto(User entity);
}
