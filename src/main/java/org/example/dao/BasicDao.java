package org.example.dao;

import java.util.Collection;
import java.util.Optional;

public interface BasicDao<T> {
    Optional<T> get(long id);

    Collection<T> getAll();

    T save(T t);

    void update(T t);

    void delete(int id);
}
