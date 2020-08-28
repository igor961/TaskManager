package org.example.dto.mappers;

public interface ModelMapper<E, D> {
    D getDto(E e);
}
