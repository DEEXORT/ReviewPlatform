package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserTo> {
}
