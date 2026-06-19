package org.yearup.exception;

/**
 * Thrown when user input fails validation or violates business rules.
 * HTTP Status: 400 BAD_REQUEST
 */

public class InvalidInputException extends ApplicationException{

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

}
