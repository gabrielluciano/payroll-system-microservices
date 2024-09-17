package com.gabrielluciano.employeeservice.domain.exception;

import lombok.Getter;

@Getter
public class DuplicatedEntityException extends RuntimeException {

    private final transient Object id;

    public DuplicatedEntityException(Object id, Class<?> clazz) {
        super(String.format("Entity of type '%s' with '%s' already exists", clazz.getSimpleName(), id));
        this.id = id;
    }
}
