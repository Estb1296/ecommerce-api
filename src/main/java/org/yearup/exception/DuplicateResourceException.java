package org.yearup.exception;

/**
 * Thrown when attempting to create a resource that already exists.
 * HTTP Status: 409 CONFLICT
 */

public class DuplicateResourceException extends ApplicationException{

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
