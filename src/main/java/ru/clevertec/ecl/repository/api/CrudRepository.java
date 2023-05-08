package ru.clevertec.ecl.repository.api;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    void delete(ID id);

    T save(T entity);

    T update(ID id, T entity);

    List<T> findAll();

    Optional<T> find(ID id);
}
