package org.example.dao;

import org.example.entities.Project;

import java.util.Collection;
import java.util.Optional;

public interface BasicDao<T> {
    Optional<T> get(int id);

    Collection<T> getAll();

    Project save(T t);

    Project update(T t);

    void delete(int id);
}
