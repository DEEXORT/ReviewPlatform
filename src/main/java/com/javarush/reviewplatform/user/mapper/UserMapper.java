package com.javarush.reviewplatform.user.mapper;

import com.javarush.reviewplatform.common.BaseMapper;
import com.javarush.reviewplatform.user.model.User;
import com.javarush.reviewplatform.user.model.UserTo;
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
