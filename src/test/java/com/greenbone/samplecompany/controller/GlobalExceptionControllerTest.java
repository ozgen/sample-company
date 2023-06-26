package com.greenbone.samplecompany.controller;

import com.greenbone.samplecompany.exception.ComputerAlreadyAssignedException;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageConversionException;

class GlobalExceptionControllerTest {

    @Test
    void handleNotFoundException_shouldReturnNotFoundMessage() {
        // given
        GlobalExceptionController controller = new GlobalExceptionController();

        // when
        String result = controller.handleNotFoundException();

        // then
        Assertions.assertEquals("Computer not found", result);
    }

    @Test
    void handleInternalError_shouldReturnInternalErrorMessage() {
        // given
        GlobalExceptionController controller = new GlobalExceptionController();

        // when
        String result = controller.handleInternalError(new Exception());

        // then
        Assertions.assertEquals("Internal error", result);
    }

    @Test
    void handleHttpMessageConversionException_shouldReturnInvalidRequestBodyMessage() {
        // given
        GlobalExceptionController controller = new GlobalExceptionController();

        // when
        String result = controller.handleHttpMessageConversionException(new HttpMessageConversionException("Invalid request body"));

        // then
        Assertions.assertEquals("Invalid request body format. Please provide a valid request body.", result);
    }

    @Test
    void handleInvalidDataException_shouldReturnInvalidAbbreviationErrorMessage() {
        // given
        GlobalExceptionController controller = new GlobalExceptionController();

        // when
        String result = controller.handleInvalidDataException(new InvalidAbbreviationException("Invalid abbreviation"));

        // then
        Assertions.assertEquals("Abbreviation is not valid: Invalid abbreviation", result);
    }

    @Test
    void handleComputerAlreadyAssignedException_shouldReturnComputerAlreadyAssignedErrorMessage() {
        // given
        GlobalExceptionController controller = new GlobalExceptionController();

        // when
        String result = controller.handleComputerAlreadyAssignedException(new ComputerAlreadyAssignedException("Computer already assigned"));

        // then
        Assertions.assertEquals("Computer is already assigned: Computer already assigned", result);
    }
}
