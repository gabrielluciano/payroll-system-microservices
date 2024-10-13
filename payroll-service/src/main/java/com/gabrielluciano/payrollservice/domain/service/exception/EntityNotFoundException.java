package com.gabrielluciano.payrollservice.domain.service.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final transient Object id;

    public EntityNotFoundException(Object id, Class<?> clazz) {
        super(String.format("Entity of type '%s' with '%s' not found", clazz.getSimpleName(), id));
        this.id = id;
    }
}
