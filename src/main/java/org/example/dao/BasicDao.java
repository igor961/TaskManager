package org.example.dao;

public interface BasicDao<T> {
    T get(long id);

    T save(T task);

    void update(T task);

    void delete(long id);
}
