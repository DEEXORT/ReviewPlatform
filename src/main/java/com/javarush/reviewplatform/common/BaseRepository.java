package com.javarush.reviewplatform.common;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E> extends CrudRepository<E, Long> {
    List<E> findAll();
}
