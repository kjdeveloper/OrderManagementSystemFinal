package com.app.repository.generic;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {

    Optional<T> addOrUpdate(T t);
    Optional<T> delete(Long id);
    List<T> deleteAll();
    Optional<T> findById(Long id);
    List<T> findAll();

}
