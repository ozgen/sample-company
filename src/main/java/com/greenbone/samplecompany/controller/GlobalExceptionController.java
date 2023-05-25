package com.greenbone.samplecompany.controller;


import com.greenbone.samplecompany.exception.ComputerAlreadyAssignedException;
import com.greenbone.samplecompany.exception.ComputerNotFoundException;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ComputerNotFoundException.class)
    public String handleNotFoundException() {
        logger.warn("Entity not found");
        return "Computer not found";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleInternalError(Exception e) {
        logger.error("Unhandled Exception in Controller", e);
        return "Internal error";
    }

    // ref : https://stackoverflow.com/questions/16651160/spring-rest-errorhandling-controlleradvice-valid
    // ref : https://stackoverflow.com/questions/33663801/how-do-i-customize-default-error-message-from-spring-valid-validation
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("Data is not valid", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public String handleHttpMessageConversionException(HttpMessageConversionException ex) {
        logger.warn("Invalid request body format", ex);
        return "Invalid request body format. Please provide a valid request body.";
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAbbreviationException.class)
    public String handleInvalidDataException(Exception e) {
        logger.warn("Abbreviation is not valid", e);
        return "Abbreviation is not valid: " + e.getMessage();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ComputerAlreadyAssignedException.class)
    public String handleComputerAlreadyAssignedException(Exception e) {
        logger.warn("Computer is already assigned", e);
        return "Computer is already assigned: " + e.getMessage();
    }
}
