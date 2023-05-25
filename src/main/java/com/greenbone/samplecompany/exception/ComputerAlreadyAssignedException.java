package com.greenbone.samplecompany.exception;

/**
 * throws when the employee abbreviation  is invalid
 */
public class ComputerAlreadyAssignedException extends RuntimeException {
    public ComputerAlreadyAssignedException(String message) {
        super(message);
    }
}
