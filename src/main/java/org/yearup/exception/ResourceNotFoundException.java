package org.yearup.exception;

/**
 * Thrown when a requested resource is not found.
 * HTTP Status: 404 NOT_FOUND
 */

public class ResourceNotFoundException extends ApplicationException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
