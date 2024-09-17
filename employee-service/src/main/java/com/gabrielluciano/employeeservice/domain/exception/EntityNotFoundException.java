package com.gabrielluciano.employeeservice.domain.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Object id, Class<?> clazz) {
        super(String.format("Entity of type '%s' with '%s' not found", clazz.getSimpleName(), id));
    }
}
