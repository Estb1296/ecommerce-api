package org.yearup.exception;

/**
 * Thrown when a database or external service operation fails unexpectedly.
 * HTTP Status: 500 INTERNAL_SERVER_ERROR
 */

public class DataAccessException extends ApplicationException{

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
