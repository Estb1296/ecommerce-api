package org.yearup.exception;

/**
 * Thrown when an operation is not allowed due to business rules or permissions.
 * HTTP Status: 403 FORBIDDEN
 */

public class UnauthorizedOperationException extends ApplicationException{

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
