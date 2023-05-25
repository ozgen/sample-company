package com.greenbone.samplecompany.exception;

/**
 * throws when the employee abbreviation  is invalid
 */
public class InvalidAbbreviationException extends RuntimeException {
    public InvalidAbbreviationException(String message) {
        super(message);
    }
}
