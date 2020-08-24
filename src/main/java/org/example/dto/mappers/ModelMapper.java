package org.example.dto.mappers;

public abstract class ModelMapper<E, D> {
    public abstract D getDto(E e);
    public abstract E getEntity(D d);
}
