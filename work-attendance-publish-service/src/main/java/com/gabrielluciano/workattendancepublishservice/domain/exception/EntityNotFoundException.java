package com.gabrielluciano.workattendancepublishservice.domain.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final transient Object id;

    public EntityNotFoundException(Object id, String entityName) {
        super(String.format("Entity of type '%s' with id '%s' not found", entityName, id));
        this.id = id;
    }
}
