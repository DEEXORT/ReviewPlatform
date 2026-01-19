package com.javarush.reviewplatform.common;

import org.mapstruct.Mapper;

import java.util.List;

public interface BaseMapper<E extends HasId, T extends BaseTo> {
    E mapToEntity(T to);
    T mapToDto(E entity);
    List<T> mapToDtoList(List<E> entities);
}
