package com.javarush.reviewplatform.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractBaseService<E extends HasId, T extends BaseTo, R extends BaseRepository<E>, M extends BaseMapper<E, T>> {
    protected final R repository;
    protected final M mapper;

    @Transactional
    public T save(T to) {
        if (to == null) throw new NullPointerException("DTO object cannot be null");

        E entity = mapper.mapToEntity(to);
        E savedEntity = repository.save(entity);
        return mapper.mapToDto(savedEntity);
    }

    public T getById(Long id) {
        if (id == null) throw new NullPointerException("id is null");

        E entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id = " + id + " not found"));
        return mapper.mapToDto(entity);
    }

    public List<T> getAll() {
        return mapper.mapToDtoList(repository.findAll());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
