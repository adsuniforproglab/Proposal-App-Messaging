package com.leonardo.propostaapp.exception;

import java.io.Serial;

/**
 * Exception for when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("%s not found with %s: '%s'".formatted(resourceName, fieldName, fieldValue));
    }
}
